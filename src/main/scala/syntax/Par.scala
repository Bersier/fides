package syntax

/**
  * All the outgoing branches shall be executed.
  */
final class Par[ValueT <: Value](o: => Set[Taker[ValueT]]) extends Taker[ValueT] {
  val out = o
}
