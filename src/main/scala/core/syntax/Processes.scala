package core.syntax

/**
  * Process
  */
trait P[+K <: I] extends Lex[K]

final case class Forward[K <: I, T <: V[K]](inp: E[K, T], out: Multiset[Out[K, T]]) extends P[K]
final case class Wait[K <: I, T <: V[K]](token: E[K, U], inp: E[K, T], out: Out[K, T]) extends P[K]

// Could be an agent instead of a primitive...
final case class Match[K <: I](inp: E[K, Code[K]], pattern: Patt, noMatch: Out[K, Code[K]]) extends P[K]

final case class Concurrent[K <: I](processes: Multiset[P[K]]) extends P[K]
final case class Replicated[K <: I](process: P[K]) extends P[K]
final case class New[K <: I](addresses: Set[Address[K]], process: P[K]) extends P[K]
final case class Awake[K <: I](name: Name, process: P[K]) extends P[K] // return name?
final case class Asleep[K <: I](name: Name, process: P[K]) extends P[K]
final case class Swappable[K <: I](inp: E[K, Code[K]], process: P[K]) extends P[K]
final case class Annotated[K <: I](process: P[K], annotation: P[_ <: I]) extends P[K]
final case class Guarded[K <: I](handler: Out[K, Error], process: P[K]) extends P[K]
final case class Shell() extends P[RegularK] {
  def send(value: Val): Unit = ???
  def register[T <: V[RegularK]](inp: Address[RegularK])(consumer: T => Unit): Unit = ???
  def registerReplicated[T <: V[RegularK]](inp: Address[RegularK])(consumer: T => Unit): Unit = ???
  def remove() = ???
}



// Or give additional type variable to Process and Val.
// using locs inside values creates mutable or lazy values; do we really want that? Or blocking values.
// Perhaps the latter is the proper semantics. Then they can just be seen as syntactic sugar for a process.
