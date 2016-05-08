package semantics

import syntax.{Idee, Receive, Value}

object Env {
  def send(a: Idee, m: Value): Unit = ???
  def addReceiver[T <: Value](receiver: Receive[T]): Unit = ???
}
