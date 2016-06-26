package syntax

class Constant[V <: Val](v: V)(val out: Taker[V]) extends UnitTaker {
  override def apply(): Unit = out(v)
}
