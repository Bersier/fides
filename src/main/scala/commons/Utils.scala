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

  final class Countdown(private[this] var i: Long)(whenZero: => Unit) {
    require(i > 0)
    i -= 1

    def tick(): Unit = nextTask()

    private var nextTask: () => Unit = task

    private[this] def task: () => Unit = () => {
      if (i >= 4) nextTask = () => nextTask = () => nextTask = () => {
        i -= 4
        nextTask = task
      }
      else if (i == 3) three()
      else if (i == 2) two()
      else if (i == 1) one()
      else whenZero
    }

    private[this] def three() = nextTask = two
    private[this] def two()   = nextTask = one
    private[this] def one()   = nextTask = () => whenZero
  }
}
