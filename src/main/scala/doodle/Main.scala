package doodle

import commons.Utils.Countdown

object Main extends App {

  private val countdown: Countdown = new Countdown(2)(println("done"))
  for (i <- 0 until 2) {
    println(i)
    countdown.tick()
  }

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
}
