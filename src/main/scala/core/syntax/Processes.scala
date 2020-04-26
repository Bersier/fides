package core.syntax

final case class Send[+K <: N, +T <: ValT](inp: L[K, Inp[T]], address: L[K, Loc[T]]) extends L[K, Pro]
final case class Forward[+K <: N, +T <: ValT](inp: L[K, Inp[T]], out: L[K, Out[T]]) extends L[K, Pro]

final case class Launch[+K <: N](
    code: Inp[K, Code[K, Inp, Val, L[K, Pro]]],
    receipt: O[K, Signed[K, Out, Code[K, Out, Val, L[K, Pro]]]],
) extends L[K, Pro]

final case class Concurrent[K <: N](processes: Multiset[L[K, Pro]]) extends L[K, Pro] // ASet
final case class Replicated[+K <: N](process: L[K, Pro]) extends L[K, Pro]
final case class New[K <: N, T <: TOP_X](iDs: Set[ID[K, Inp with Out, T]], process: L[K, Pro]) extends L[K, Pro] //X[K, Val, ASet]
final case class Awake[+K <: N](name: Name, process: L[K, Val, L[K, Pro]]) extends L[K, Pro] // Also replace L[K, Pro] with L[K, Val, L[K, Pro]] in others
final case class Asleep[+K <: N](name: Name, process: L[K, Pro]) extends L[K, Pro]
final case class Swappable[+K <: N](inp: I[K, Code[K, Inp, Val, L[K, Pro]]], process: L[K, Pro]) extends L[K, Pro]
final case class Annotated[+K <: N](process: L[K, Pro], annotation: X[K, D, TOP_X]) extends L[K, Pro]
final case class Handled[+K <: N](process: L[K, Pro], handler: O[K, Error]) extends L[K, Pro]
final case class Shell() extends P[RegularK] {
  def send(value: Val): Unit = ???
  def register[T <: V[RegularK, T]](inp: Loc[RegularK, T])(consumer: T => Unit): Unit = ???
  def registerReplicated[T <: V[RegularK, T]](inp: Loc[RegularK, T])(consumer: T => Unit): Unit = ???
  def remove(): Unit = ???
}
