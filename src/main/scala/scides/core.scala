package scides

import util.Async

import scala.collection.mutable

type Consumer[T] = T => Async
type Message = Any

trait Scidesphere:
  private[this] val messagesInTransit = mutable.HashMap.empty[Channel, mutable.ArrayBuffer[Message]]
  private[this] val registeredRecipients = mutable.HashMap.empty[Channel, mutable.ArrayBuffer[Consumer[Message]]]

  class Channel
  class Key:
    val channel: Channel = new Channel

  case class Signed private(message: Message, signature: Channel)
  object Signed:
    def apply(message: Message, key: Key): Signed = Signed(message, key.channel)

  trait Compiler extends Channel

  def step(): Unit

  def run(): Unit =
    for (channel, messages) <- messagesInTransit do
      registeredRecipients.get(channel).foreach(arrayBuffer =>
        for message <- messages do
          // todo
          arrayBuffer.pop()
      )

  def send(message: Message, recipient: Channel): Async = Async{
    messagesInTransit.getOrElseUpdate(recipient, mutable.ArrayBuffer.empty[Message]) += message
  }

  def register(key: Key, recipient: Consumer[Message]): Unit =
    registeredRecipients.getOrElseUpdate(key.channel, mutable.ArrayBuffer.empty[Consumer[Message]]) += recipient

  def call(message: Message, recipient: Channel): Message
  // def call(message: Message, recipient: Channel, answerHandler: Consumer[Message]): Async

  def registerCallee(key: Key, callee: Message => Message): Unit
