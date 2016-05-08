package syntax

/**
  * Will execute as soon as one of the incoming branches is done.
  */
sealed trait With[-OuT <: Value] {
  def in: With.In[OuT]
}

object With {
  def apply[OuT <: Value](out: => Taker[OuT]): With[OuT] = new With.In(out)

  final class In[-OuT <: Value] private[With](o: => Taker[OuT]) extends With[OuT] with Taker[OuT] {
    val out = o

    def in = this
  }
}
