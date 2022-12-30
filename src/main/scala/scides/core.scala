package scides

import util.Async

import scala.collection.{concurrent, mutable}

type Consumer[T] = T => Async
type Message = Any

trait Scidesphere:
  private[this] var messagesInTransit = mutable.HashMap.empty[Channel, mutable.ArrayBuffer[Message]]
  private[this] val registeredRecipients = concurrent.TrieMap.empty[Channel, Consumer[Message]]

  class Channel:
    val inTransit: mutable.ArrayBuffer[Message] // todo decentralize like this? Try to get rid of all bottlenecks...
  class Key:
    val channel: Channel = new Channel

  case class Signed private(message: Message, signature: Channel)
  object Signed:
    def apply(message: Message, key: Key): Signed = Signed(message, key.channel)

  trait Compiler extends Channel

  def step(): Unit

  def deliverAll(): Unit =
    val allMessages = synchronized {
      val allMessages = messagesInTransit
      messagesInTransit = mutable.HashMap.empty
      allMessages
    }
    for (channel, messages) <- allMessages
        message <- messages
    do deliver(message, channel) // todo add concurrency

  def send(message: Message, recipient: Channel): Async = Async { synchronized {
    messagesInTransit.getOrElseUpdate(recipient, mutable.ArrayBuffer.empty[Message]) += message
  }}

  private[this] inline def deliver(message: Message, recipient: Channel): Unit =
    registeredRecipients.get(recipient).foreach(_(message))

  def register(recipient: Consumer[Message]): Key =
    val key = new Key
    registeredRecipients(key.channel) = recipient
    key

  def deregister(key: Key): Unit =
    registeredRecipients.remove(key.channel)

// ------------------------------------------------------------

  def call(message: Message, recipient: Channel): Message
  // def call(message: Message, recipient: Channel, answerHandler: Consumer[Message]): Async

  def registerCallee(key: Key, callee: Message => Message): Unit
