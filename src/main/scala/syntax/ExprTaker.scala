package syntax

/**
  * ExprTaker extends UnitTaker because Unit is a 'subtype' of Expr and because of contravariance.
  */
trait ExprTaker extends UnitTaker {
  def apply: Value => Unit = ??? // apply (not needed to model the syntax)
}
