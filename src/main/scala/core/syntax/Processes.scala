package core.syntax

sealed trait Proc[+K <: I] extends Lex[K]

final case class Forward[K <: I, T <: V[K]](inLoc: Inp[K, T], outLocs: Multiset[Out[K, T]]) extends Proc[K]
final case class Wait[K <: I, T <: V[K]](token: Inp[K, U], inLoc: Inp[K, T], outLoc: Out[K, T]) extends Proc[K]

// Could be an agent instead of a primitive...
final case class Match[K <: I](inLoc: Inp[K, Code[K]], pattern: Patt, noMatch: Out[K, Code[K]]) extends Proc[K]

final case class Concurrent[K <: I](processes: Multiset[Proc[K]]) extends Proc[K]
final case class Replicated[K <: I](process: Proc[K]) extends Proc[K]
final case class New[K <: I](addresses: Set[Address[K]], process: Proc[K]) extends Proc[K]
final case class Awake[K <: I](name: Name, process: Proc[K]) extends Proc[K] // return name?
final case class Asleep[K <: I](name: Name, process: Proc[K]) extends Proc[K]
final case class Swappable[K <: I](inLoc: Inp[K, Code[K]], process: Proc[K]) extends Proc[K]
final case class Annotated[K <: I](process: Proc[K], annotation: Proc[_ <: I]) extends Proc[K]
final case class Guarded[K <: I](handler: Out[K, Error], process: Proc[K]) extends Proc[K]
final case class Shell() extends Proc[RegularK] {
  def send(value: Val): Unit = ???
  def register[T <: V[RegularK]](inLoc: Address[RegularK])(consumer: T => Unit): Unit = ???
  def registerReplicated[T <: V[RegularK]](inLoc: Address[RegularK])(consumer: T => Unit): Unit = ???
  def remove() = ???
}

final case class Var[K <: I](group: Address[K], outLoc: Out[K, V[K]]) extends
  Proc[PatternK] with
  Loc[PatternK, Nothing] // with ...

// Or give additional type variable to Process and Val.
// using locs inside values creates mutable or lazy values; do we really want that? Or blocking values.
// Perhaps the latter is the proper semantics. Then they can just be seen as syntactic sugar for a process.
