package core.syntax

final case class Send[+K <: N, T <: A](inp: L[K, Inp[T]], address: L[K, Inp[Code[_, Inp, Loc[T]]]])
  extends L[K, Prs]
final case class Receive[+K <: N, T <: A](out: L[K, Inp[T]], dual(address): L[K, Inp[Code[_, Inp, Loc[T]]]])
  extends L[K, Prs]
// Can be achieve using two sends: Send(<a>, <b>)|Send(r, <a>)|Forward(a, out)
//                                 Send(<out>, <b>)|Send(r, <out>)
//                            for: Forward(r, b)|Receive(out, <b>)
//                      If r == b: Receive(out, <b>)

final case class Forward[+K <: N, T <: A](inp: L[K, Inp[T]], out: L[K, Out[T]]) extends L[K, Prs]

// Make inp and out first order?

final case class Launch[+K <: N](
  code: L[K, Inp[Code[K, Inp, Prs]]],
  receipt: L[K, Out[Signed[_, Code[_, Out, Prs], Out]]],
) extends L[K, Prs]

// TODO Reflect?

final case class After(inp: Any, out1: Any, out2: Any)

final case class Replicated[+K <: N](process: L[K, Prs]) extends L[K, Prs]
final case class New[+K <: N](iDs: L[K, Val[Bag[_, IdeT, Val]]], process: L[K, Prs]) extends L[K, Prs]
final case class Awake[+K <: N](name: Name, process: L[K, Prs]) extends L[K, Prs]
final case class Asleep[+K <: N](name: Name, process: L[K, Prs]) extends L[K, Prs]
final case class Handled[+K <: N](process: L[K, Prs], handler: L[K, Out[A]]) extends L[K, Prs]

final case class Shell() extends L[RegularK, Prs] {
  def send(value: V[_]): Unit = ???
  def register[T <: A](consumer: T => Unit): LocKey[T] = ???
  def registerReplicated[T <: A](consumer: T => Unit): LocKey[T] = ???
  def remove(): Unit = ???
}
