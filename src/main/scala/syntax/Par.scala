package syntax

/**
  * Will only execute once all the incoming branches are done.
  */
sealed trait Par {
  def in: Par.In
}

object Par {
  def apply(size: Int)(out: UntTaker): Par = new Par.In(size)(out)

  /**
    * @param size number of incoming branches
    */
  final class In private[Par] (size: Int)(val out: UntTaker) extends Par with UntTaker {
    private[this] var counter = 0

    def in = {
      val i = Utils.unused(counter < size)(this)
      counter += 1
      i
    }
  }
}
