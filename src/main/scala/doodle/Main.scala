package doodle

object Main extends App {

  val f: Int => Set[Int] = ???
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