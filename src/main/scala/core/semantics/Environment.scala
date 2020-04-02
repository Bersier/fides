package core.semantics

import core.syntax._

import scala.collection.mutable

final class Env {
  private[this] val messages: mutable.Set[Message[_]] = ???
  private[this] val receivers: mutable.Map[Loc[Val], Multiset[]] = ???

  def send[T <: Val](value: T, recipient: OutLoc[T]): Unit = {
    messages += new Message(value, recipient)
  }

  def register[R: Receiver](inLoc: InLoc[_], receiver: R): Unit = {
    val r = implicitly[Receiver[R]];
    receivers(inLoc)(receiver) += 1
  }
}

final class Message[+T <: Val](val value: T, val recipient: InLoc[T])

trait Receiver[R] {
  type T <: Val
  def receive(message: T): Unit
}
