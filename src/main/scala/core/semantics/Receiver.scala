package core.semantics

import core.syntax
import core.syntax.Process

trait Receiver extends Any {
  def receive(message: Val): (Multiset[Receiver], Set[Message], Multiset[Process])
}

class Swappable(val value: syntax.Swappable) extends AnyVal with Receiver {
  override def receive(message: Val): (Multiset[Receiver], Set[Message], Multiset[Process]) = ???
}
