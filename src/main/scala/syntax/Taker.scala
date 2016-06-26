package syntax

trait Taker[-ValueT <: Val] {
  def apply(v: ValueT): Unit
}

trait UnitTaker extends Taker[Val] {
  def apply(): Unit

  final def apply(ignored: Val): Unit = apply()
}
