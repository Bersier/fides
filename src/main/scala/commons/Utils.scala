package commons

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object Utils {

  def unused[A](used: Boolean)(a: => A): A = {
    if (used) throw new IllegalStateException(a + " already used.")
    else a
  }

  def throwAlreadyUsedException[A](aToString: String): A = {
    throw new IllegalStateException(aToString + " already used.")
  }

  def concurrently(task1: => Unit, task2: => Unit): Unit = {
    Future(task1)
    task2
  }
}
