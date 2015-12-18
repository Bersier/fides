package syntax

/**
  * All the outgoing branches shall be executed.
  */
final class Par(val out: Set[ExprTaker]) extends ExprTaker
