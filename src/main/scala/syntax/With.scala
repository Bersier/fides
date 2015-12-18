package syntax

/**
  * Will execute as soon as one of the incoming branches is done.
  */
sealed trait With {
  def in: With.In
}

object With {
  def apply(out: UnitTaker): With = new With.In(out)

  final class In private[With](val out: UnitTaker) extends With with UnitTaker {
    def in = this
  }
}
