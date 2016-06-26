package syntax.flowcontrol

import syntax.{Taker, Val}

/**
  * Will execute as soon as one of the incoming branches is done.
  */
sealed trait With[-OuT <: Val] {
  def in: With.In[OuT]
}

object With {
  def apply[OuT <: Val](out: => Taker[OuT]): With[OuT] = new With.In(out)

  final class In[-OuT <: Val] private[With](o: => Taker[OuT]) extends With[OuT] with Taker[OuT] {
    val out = o

    def in = this

    override def apply(v: OuT): Unit = todo

    private[this] var todo: OuT => Unit = v => {
      out(v)
      todo = _ => {}
    }
  }
}
