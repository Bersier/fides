package scides

import util.Async

import java.util.concurrent.ConcurrentLinkedQueue
import javax.sound.sampled.Clip
import scala.collection.{concurrent, mutable}
import scala.concurrent.{Future, ExecutionContext}
import ExecutionContext.Implicits.global

type Consumer[T] = T => Async
type Message = Any
val NoOp: Consumer[Message] = m => Async()

trait Scidesphere:
  class Channel:
    @volatile private[this] var registered: Consumer[Message] = NoOp

    def send(message: Message): Async = Async(Future(registered(message)))

    private[this] object Key extends Scidesphere.this.Key:
      def channel: Channel = Channel.this

      def register(recipient: Consumer[Message]): Unit =
        registered = recipient

      def deregister(): Unit =
        registered = NoOp

  object Channel:
    def newKey: Key = (new Channel).Key

  sealed trait Key:
    def channel: Channel
    def register(recipient: Consumer[Message]): Unit
    def deregister(): Unit

  case class Signed private(message: Message, signature: Channel)
  object Signed:
    def apply(message: Message, key: Key): Signed = Signed(message, key.channel)

  trait Compiler extends Channel
