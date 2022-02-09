package core

import scala.collection.mutable

final class Environment {
  type Address = Expr
  type Value = Expr

  final class Message(val value: Value, val destination: Address)
  final class Recipient {}
  
  private[this] val messages   = mutable.Set.empty[Message]
  private[this] val recipients = mutable.Map.empty[Address, Multiset[Recipient]]

  def step(e: Expr): Expr = e match {
    case C(header, C(C(inHead, inTail), outs)) => {
      C(header, C(C(step(inHead), inTail), outs))
    }
    case _                                     => e
  }

  def top(e: Expr): Unit = e match {
    case C(Forward, C(in, out)) => ()
    case _                      => ()
  }

  def input(e: Expr): Unit = e match {
    case _ => ()
  }

  def output(e: Expr): Unit = e match {
    case _ => ()
  }
}
