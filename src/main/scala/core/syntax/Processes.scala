package core.syntax

final case class Send[+K <: N, +T <: A](inp: L[K, Inp[T]], address: L[K, Inp[AsValue[K, T, Loc[T]]]]) extends L[K, Prs]
final case class Forward[+K <: N, +T <: A](inp: L[K, Inp[T]], out: L[K, Out[T]]) extends L[K, Prs]

final case class Launch[+K <: N](
    code: L[K, Inp[Code[K, Inp, Prs]]],
    receipt: L[K, Out[Signed[K, Code[K, Out, Prs], Out]]],
) extends L[K, Prs]

final case class Replicated[+K <: N](process: L[K, Prs]) extends L[K, Prs]
final case class New[K <: N](iDs: L[K, Val[Bag[K, IdeT, Val]]], process: L[K, Prs]) extends L[K, Prs]
final case class Awake[+K <: N](name: Name, process: L[K, Prs]) extends L[K, Prs]
final case class Asleep[+K <: N](name: Name, process: L[K, Prs]) extends L[K, Prs]
final case class Swappable[+K <: N](inp: L[K, Inp[Code[K, Inp, Prs]]], process: L[K, Prs]) extends L[K, Prs]
final case class Handled[+K <: N](process: L[K, Prs], handler: L[K, Out[Error]]) extends L[K, Prs]

final case class Shell() extends L[RegularK, Prs] {
  def send(value: V[_]): Unit = ???
  def register[T <: A](consumer: T => Unit): LocKey[T] = ???
  def registerReplicated[T <: A](consumer: T => Unit): LocKey[T] = ???
  def remove(): Unit = ???
}
