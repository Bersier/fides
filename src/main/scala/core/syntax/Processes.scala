package core.syntax

final case class Send[+K <: N, +T <: ValT](inp: L[K, Inp[T]], address: L[K, Loc[T]]) extends L[K, Prs]
final case class Forward[+K <: N, +T <: ValT](inp: L[K, Inp[T]], out: L[K, Out[T]]) extends L[K, Prs]

final case class Launch[+K <: N, C2 <: D](
    code: L[K, Inp[Code[K, Inp, C2]]],
    receipt: L[K, Out[Signed[K, Code[K, Out, C2], Out]]],
) extends L[K, Prs]

final case class Concurrent[K <: N](processes: Multiset[L[K, Prs]]) extends L[K, Prs]
//final case class Concurrent[K <: N](processes: L[K, ASet[K, Prs]]) extends L[K, Prs]
final case class Replicated[+K <: N](process: L[K, Prs]) extends L[K, Prs]
final case class New[K <: N](iDs: Set[L[K, Ide[_]]], process: L[K, Prs]) extends L[K, Prs] //X[K, Val, ASet]
final case class Awake[+K <: N](name: Name, process: L[K, Prs]) extends L[K, Prs]
final case class Asleep[+K <: N](name: Name, process: L[K, Prs]) extends L[K, Prs]
final case class Swappable[+K <: N](inp: L[K, Inp[Code[K, Inp, Prs]]], process: L[K, Prs]) extends L[K, Prs]
final case class Handled[+K <: N](process: L[K, Prs], handler: L[K, Out[Error]]) extends L[K, Prs]

final case class Shell() extends L[RegularK, Prs] {
  def send(value: V[_]): Unit = ???
  def register[T <: V[T]](consumer: T => Unit): LocKey[T] = ???
  def registerReplicated[T <: V[T]](consumer: T => Unit): LocKey[T] = ???
  def remove(): Unit = ???
}
