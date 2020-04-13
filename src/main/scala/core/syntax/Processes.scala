package core.syntax

/**
  * Process
  */
trait P[+K <: N] extends L[K, Val, P[K]]

final case class Send[+K <: N, +T <: TOP_X](inp: I[K, T], address: I[K, X[K, D, Loc[K, TOP_X]]]) extends P[K]
final case class Forward[+K <: N, +T <: TOP_X](inp: I[K, T], out: O[K, T]) extends P[K]

final case class Launch[+K <: N](
    code: I[K, Code[K, Inp, Val, P[K]]],
    receipt: O[K, Signed[K, Out, Code[K, Out, Val, P[K]]]],
) extends P[K]

final case class Concurrent[K <: N](processes: Multiset[P[K]]) extends P[K] // ASet
final case class Replicated[+K <: N](process: P[K]) extends P[K]
final case class New[K <: N, T <: TOP_X](iDs: Set[ID[K, Inp with Out, T]], process: P[K]) extends P[K] //X[K, Val, ASet]
final case class Awake[+K <: N](name: Name, process: L[K, Val, P[K]]) extends P[K] // Also replace P[K] with L[K, Val, P[K]] in others
final case class Asleep[+K <: N](name: Name, process: P[K]) extends P[K]
final case class Swappable[+K <: N](inp: I[K, Code[K, Inp, Val, P[K]]], process: P[K]) extends P[K]
final case class Annotated[+K <: N](process: P[K], annotation: P[N]) extends P[K]
final case class Guarded[+K <: N](process: P[K], handler: O[K, Error]) extends P[K]
final case class Shell() extends P[RegularK] {
  def send(value: Val): Unit = ???
  def register[T <: V[RegularK, T]](inp: Loc[RegularK, T])(consumer: T => Unit): Unit = ???
  def registerReplicated[T <: V[RegularK, T]](inp: Loc[RegularK, T])(consumer: T => Unit): Unit = ???
  def remove(): Unit = ???
}
