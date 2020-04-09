package core.syntax

/**
  * Process
  */
trait P[+K <: N] extends Lex[K]

final case class Forward[K <: N, T <: V[K]](inp: E[K], out: O[K, T]) extends P[K]
final case class Wait[K <: N, T <: V[K]](token: E[K], inp: E[K], out: O[K, T]) extends P[K]

final case class Concurrent[K <: N](processes: Multiset[P[K]]) extends P[K]
final case class Replicated[K <: N](process: P[K]) extends P[K]
final case class New[K <: N](addresses: Set[Address[K]], process: P[K]) extends P[K]
final case class Awake[K <: N](name: Name, process: P[K]) extends P[K] // return name?
final case class Asleep[K <: N](name: Name, process: P[K]) extends P[K]
final case class Swappable[K <: N](inp: E[K], process: P[K]) extends P[K]
final case class Annotated[K <: N](process: P[K], annotation: P[_ <: N]) extends P[K]
final case class Guarded[K <: N](handler: O[K, Error], process: P[K]) extends P[K]
final case class Shell() extends P[RegularK] {
  def send(value: Val): Unit = ???
  def register[T <: V[RegularK]](inp: Address[RegularK])(consumer: T => Unit): Unit = ???
  def registerReplicated[T <: V[RegularK]](inp: Address[RegularK])(consumer: T => Unit): Unit = ???
  def remove() = ???
}



// Or give additional type variable to Process and Val.
// using locs inside values creates mutable or lazy values; do we really want that? Or blocking values.
// Perhaps the latter is the proper semantics. Then they can just be seen as syntactic sugar for a process.
