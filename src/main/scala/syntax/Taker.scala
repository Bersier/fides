package syntax

trait Taker[-ValueT <: Val] {
  def apply(v: ValueT): Unit
}

trait UnitTaker extends Taker[Val] {
  def apply(): Unit

  def apply(ignored: Val): Unit = apply()
}