package scides

import util.Async
import scala.concurrent.{Future, ExecutionContext}
import scala.quoted.{Expr, staging}
import ExecutionContext.Implicits.global

given staging.Compiler = staging.Compiler.make(getClass.getClassLoader.nn)

type Consumer[T] = T => Async
val NoOp: Consumer[Any] = m => Async()

final class Scidesphere:
  sealed trait Channel[-T]:
    def send(message: T): Async
  object Channel:
    def apply[T](): Channel[T] = new UserChannel
    def newKey[T]: Key[T] = UserChannel.newKey
  end Channel

  sealed trait Key[T]:
    def channel: Channel[T]
    def register(recipient: Consumer[T]): Unit
    def deregister(): Unit
  end Key

  private[Scidesphere] final class UserChannel[T] extends Channel[T]:
    @volatile protected[this] var registered: Consumer[T] = NoOp

    def send(message: T): Async = Async(Future(registered(message)))

    private[this] object Key extends Scidesphere.this.Key[T]:
      def channel: Channel[T] = UserChannel.this

      def register(recipient: Consumer[T]): Unit =
        registered = recipient

      def deregister(): Unit =
        registered = NoOp
    end Key
  private[Scidesphere] object UserChannel:
    def newKey[T]: Key[T] = (new UserChannel).Key
  end UserChannel

  final case class Signed[T] private(message: T, signature: PublicKey)
  object Signed:
    def apply[T](message: T, key: PrivateKey): Signed[T] = Signed(message, key.channel)
  end Signed
  type PrivateKey = Key[?]
  type PublicKey = Channel[?]

  final class Launcher extends Channel[LauncherInput]:
    def send(message: LauncherInput): Async = Async(Future{
      val (code, certificateRecipient) = message
      certificateRecipient.send(Signed(code, Key))
      staging.run(code)
    })

    private[this] object Key extends Scidesphere.this.Key[LauncherInput]:
      def channel: Channel[LauncherInput] = Launcher.this
      def register(recipient: Consumer[LauncherInput]): Unit = throw new AssertionError()
      def deregister(): Unit = throw new AssertionError()
    end Key
  end Launcher
  type LauncherInput = (Expr[Unit], Channel[Signed[Expr[Unit]]])
end Scidesphere
