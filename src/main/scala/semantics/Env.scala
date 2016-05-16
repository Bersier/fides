package semantics

import syntax.{Idee, Receive, Val}

object Env {
  def send(a: Idee, m: Val): Unit = ???
  def addReceiver[T <: Val](receiver: Receive[T]): Unit = ???
}
