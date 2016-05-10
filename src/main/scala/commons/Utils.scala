package commons

object Utils {

  def unused[A](used: Boolean)(a: => A): A = {
    if (used) throw new IllegalStateException(a + " already used.")
    else a
  }

  def thowAlreadyUsedException[A](aToString: String): A = {
    throw new IllegalStateException(aToString + " already used.")
  }
}
