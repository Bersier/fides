package scides

import util.Async

import java.util.concurrent.ConcurrentLinkedQueue
import javax.sound.sampled.Clip
import scala.collection.{concurrent, mutable}

type Consumer[T] = T => Async
type Message = Any

trait Scidesphere:

  class Channel:
    private[this] val inTransit: ConcurrentLinkedQueue[Message] = new ConcurrentLinkedQueue
    @volatile private[this] var registered: Option[Consumer[Message]] = None

    def send(message: Message): Async = Async(inTransit.add(message))

    private[this] inline def step(): Unit =
      Option(inTransit.poll()).foreach(deliver(_))

    private[this] inline def deliver(inline message: Message): Unit =
      registered.foreach(_(message))

    private[this] object Key extends Scidesphere.this.Key:
      def channel: Channel = Channel.this

      def register(recipient: Consumer[Message]): Unit =
        registered = Some(recipient)

      def deregister(): Unit =
        registered = None

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
