package simplest

object Semantics {

  def top(e: Expr): Unit = e match {
    case C(Forward, C(in, out)) => ()
    case _ => ()
  }

  def incoming(e: Expr): Unit = e match {
    case _ => ()
  }
}
