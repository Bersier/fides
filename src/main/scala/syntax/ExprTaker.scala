package syntax

/**
  * ExprTaker extends UnitTaker because Unit is a 'subtype' of Expr and because of contravariance.
  */
trait ExprTaker extends UnitTaker { // what about higher-order use of primitive agents?
  def apply: Value => Unit = ??? // apply (not needed to model the syntax)
}
