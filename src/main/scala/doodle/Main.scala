package doodle

object Main extends App {

  val f: Int => Set[Int] = ???
  val g: Int =-> Int = =->(f)
  val a: Actor = Actor(=->(m => Set((Set(m), a))))

  sealed trait Message

  trait I[M[_ <: I[M]]]

  g.f

  =->(f).f

  =->(g.f)

  final case class =->[A, B](f: A => Set[B])

  final case class Actor(f: Message =-> (Set[Message], Actor))
}
