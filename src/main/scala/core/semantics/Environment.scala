package core.semantics

import scala.collection.mutable

final class Message(val value: Val, val recipient: Loc)

final class Environment {
  private[this] val messages: mutable.Set[Message] = mutable.Set.empty
  private[this] val receivers: mutable.Map[Loc, mutable.Map[Receiver, BigInt]] = mutable.Map.empty

  def send(value: Val, recipient: Loc): Unit = {
    messages += new Message(value, recipient)
  }

  def register(inLoc: Loc, receiver: Receiver): Unit = {
    receivers.getOrElseUpdate(inLoc, mutable.Map.empty)
      .updateWith(receiver)(o => Some(o.getOrElse(BigInt(0)) + 1))
  }
}
