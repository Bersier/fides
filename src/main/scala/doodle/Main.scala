package doodle

object Main extends App {

  private var continue = true
  private val countdown = new NaiveCountdown(2000000000)({continue = false})
  println("Start")
  while (continue) {
    countdown.tick()
  }
  println("Done.")

  val f: Int => Set[Int] = _ => Set()
  val g: Int =-> Int = =->(f)
  val a: Actor = Actor(=->(m => Set((Set(m), a))))

  sealed trait Message

  trait I[M[_ <: I[M]]]

  val v1 = g.f

  val v2 = =->(f).f

  val v3 = =->(g.f)

  final case class =->[A, B](f: A => Set[B])

  final case class Actor(f: Message =-> (Set[Message], Actor))

  final class NaiveCountdown(count: Long)(whenZero: => Unit) {
    private[this] var i = count - 1

    def tick(): Unit = if (i == 0) whenZero else i -= 1
  }

  final class Countdown(count: Long)(whenZero: => Unit) {
    require(count > 0)
    private[this] var i = count - 1

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
