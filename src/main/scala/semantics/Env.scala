package semantics

import syntax.{Address, Receive, Val}

object Env {
  def send(a: Address, m: Val): Unit = ???
  def addReceiver[T <: Val](receiver: Receive[T]): Unit = ???
}
