package core.syntax

trait X[+K <: N, C <: D, T <: V[T]] extends Lex[K]

final case class APair[K <: N, C <: D, T1 <: X[K, C, V[T1]], T2 <: X[K, C, V[T2]]]
(first: T1, second: T2) extends X[K, C, V[APair[K, C, T1, T2]]]

final case class Signed[+K <: N, T <: E[K]] private(contents: T, signatory: Signatory) extends E[K] {
  def this(message: T, signatoryKey: SignatoryKey) = this(message, signatoryKey.signatory)
}
object Signed {
  def pattern[S <: G, T <: E[PattK[S]]](contents: T, signatory: Signatory): Signed[PattK[S], T] = {
    new Signed(contents, signatory)
  }
}

final case class Copy(outs: Seq[O[_, _]]) extends R[_]

final case class Match(patterns: Seq[O[_, _]]) extends R[_]
