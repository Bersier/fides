package syntax

trait Taker[-ValueT <: Value] {
  def apply(v: ValueT): Unit
}

trait UnitTaker extends Taker[Value] {
  def apply(): Unit

  def apply(ignored: Value): Unit = apply()
}

trait ExprTaker extends Taker[Value]