package core.semantics

import scala.collection.mutable

final class Env {
  private[this] val messages: mutable.Set[Message] = ???
  private[this] val receivers: mutable.Map[Loc, Multiset[_]] = ???

  def send(value: Val, recipient: Loc): Unit = {
    messages += new Message(value, recipient)
  }

  def register[R: Receiver](inLoc: Loc, receiver: R): Unit = {
    val r = implicitly[Receiver[R]];
    receivers(inLoc)(receiver) += 1
  }
}

final class Message(val value: Val, val recipient: Loc)

trait Receiver[R] {
  def receive(message: Val, r: R): Unit
}
