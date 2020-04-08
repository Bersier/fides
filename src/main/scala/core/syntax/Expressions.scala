package core.syntax

trait X[+K <: N, C <: D] extends Lex[K]

final case class InVar[K[_] <: Kind[_], T <: V[K]](inp: Inp[K[A], T]) extends E[K[G]] // Do I only need this for code?

final case class APair[K <: N, T1 <: E[K], T2 <: E[K]](first: T1, second: T2) extends E[K]

final case class Signed[+K <: N, T <: E[K]] private(contents: T, signatory: Signatory) extends E[K] {
  def this(message: T, signatoryKey: SignatoryKey) = this(message, signatoryKey.signatory)
}
object Signed {
  def pattern[S <: G, T <: E[PattK[S]]](contents: T, signatory: Signatory): Signed[PattK[S], T] = {
    new Signed(contents, signatory)
  }
}

final case class Duplicate(out1: Out[_, _], out2: Out[_, _]) extends R[_]