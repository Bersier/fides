package core.syntax

// Make processes expressions?

/**
  * Process
  */
trait P[+K <: N] extends L[K, Val, P[K]]

final case class Forward[+K <: N, +T <: X[K, D, T]](inp: I[K, T], out: O[K, T]) extends P[K]

final case class Concurrent[K <: N](processes: Multiset[P[K]]) extends P[K]
final case class Replicated[+K <: N](process: P[K]) extends P[K]
final case class New[K <: N, T <: X[K, D, T]](locs: Set[Loc[K, T]], process: P[K]) extends P[K]
final case class Awake[+K <: N](name: Name, process: P[K]) extends P[K]
final case class Asleep[+K <: N](name: Name, process: P[K]) extends P[K]
final case class Swappable[+K <: N](inp: I[K, Code[K, Inp, Val, P[K]]], process: P[K]) extends P[K]
final case class Annotated[+K <: N](process: P[K], annotation: P[N]) extends P[K]
final case class Guarded[+K <: N](process: P[K], handler: O[K, Error]) extends P[K]
final case class Shell() extends P[RegularK] {
  def send(value: Val): Unit = ???
  def register[T <: V[RegularK, T]](inp: Loc[RegularK, T])(consumer: T => Unit): Unit = ???
  def registerReplicated[T <: V[RegularK, T]](inp: Loc[RegularK, T])(consumer: T => Unit): Unit = ???
  def remove() = ???
}



// Or give additional type variable to Process and Val.
// using locs inside values creates mutable or lazy values; do we really want that? Or blocking values.
// Perhaps the latter is the proper semantics. Then they can just be seen as syntactic sugar for a process.
