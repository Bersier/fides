package syntax

/**
  * Will only execute once all the incoming branches are done.
  */
sealed trait Times {
  def in: Times.In
}

object Times {
  def apply(size: Int)(out: UnitTaker): Times = new Times.In(size)(out)

  /**
    * @param size number of incoming branches
    */
  final class In private[Times$](size: Int)(val out: UnitTaker) extends Times with UnitTaker {
    private[this] var counter = 0

    def in = {
      val i = Utils.unused(counter < size)(this)
      counter += 1
      i
    }
  }
}
