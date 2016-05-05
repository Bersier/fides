package syntax

/**
  * Only one of the outgoing branches shall be executed,
  * without control of which one (internal choice).
  */
final class Plus(val out: Set[ExprTaker]) extends ExprTaker {
  require(out.nonEmpty)
}
