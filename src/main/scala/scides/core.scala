package scides

import util.Async

import java.util.concurrent.ConcurrentLinkedQueue
import javax.sound.sampled.Clip
import scala.collection.{concurrent, mutable}
import scala.concurrent.{Future, ExecutionContext}
import scala.quoted.*
import ExecutionContext.Implicits.global

given staging.Compiler = staging.Compiler.make(getClass.getClassLoader.nn)

type Consumer[T] = T => Async
type Message = Any
val NoOp: Consumer[Message] = m => Async()

trait Scidesphere:
  sealed trait Channel:
    def send(message: Message): Async
  object Channel:
    def apply(): Channel = new UserChannel
    def newKey: Key = UserChannel.newKey
  end Channel

  sealed trait Key:
    def channel: Channel
    def register(recipient: Consumer[Message]): Unit
    def deregister(): Unit
  end Key

  private[Scidesphere] final class UserChannel extends Channel:
    @volatile protected[this] var registered: Consumer[Message] = NoOp

    def send(message: Message): Async = Async(Future(registered(message)))

    private[this] object Key extends Scidesphere.this.Key:
      def channel: Channel = UserChannel.this

      def register(recipient: Consumer[Message]): Unit =
        registered = recipient

      def deregister(): Unit =
        registered = NoOp
    end Key
  private[Scidesphere] object UserChannel:
    def newKey: Key = (new UserChannel).Key
  end UserChannel

  final case class Signed private(message: Message, signature: Channel)
  object Signed:
    def apply(message: Message, key: Key): Signed = Signed(message, key.channel)
  end Signed

  final class Executer extends Channel:
    def send(message: Message): Async = Async(Future{
      message match
        case (code: Expr[?], certificateRecipient: Channel) => {
          certificateRecipient.send(Signed(code, Key))
          staging.run(code)
        }
        case _ => ()
    })

    private[this] object Key extends Scidesphere.this.Key:
      def channel: Channel = Executer.this
      def register(recipient: Consumer[Message]): Unit = throw new AssertionError()
      def deregister(): Unit = throw new AssertionError()
    end Key
  end Executer
end Scidesphere
