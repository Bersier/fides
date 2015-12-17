package syntax

object Utils {

  def unused[A](used: Boolean)(a: => A): A = {
    if (used) throw new IllegalStateException(a + " already used.")
    else a
  }
}
