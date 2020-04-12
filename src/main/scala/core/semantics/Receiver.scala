//package core.semantics
//
//import core.syntax
//import core.syntax.RegularK
//
//trait Receiver extends Any {
//  def receive(message: Val): (Multiset[Receiver], Set[Message], Multiset[Proc])
//}
//
//class Swappable(val value: syntax.Swappable[RegularK]) extends AnyVal with Receiver {
//  override def receive(message: Val): (Multiset[Receiver], Set[Message], Multiset[Proc]) = ???
//}
