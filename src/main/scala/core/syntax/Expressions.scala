package core.syntax

trait X[+K <: N, +C <: D, +T <: X[K, D, T]] extends Lex[K, C, T]

final case class Constant[+K <: N, +T <: X[K, Val, T]](value: T) extends X[K, Val, T]

final case class APair[+K <: N, +C <: D, +T1 <: X[K, C, V[T1]], +T2 <: X[K, C, V[T2]]]
(first: T1, second: T2) extends X[K, C, APair[K, C, T1, T2]]

final case class Signed[+K <: N, +C <: D, +T <: X[K, C, T]] private(contents: T, signatory: Signatory)
  extends X[K, C, Signed[K, C, T]] {
  def this(message: T, signatoryKey: SignatoryKey) = this(message, signatoryKey.signatory)
}
object Signed {
  def out[K <: N, T <: O[K, T]](contents: T, signatory: Signatory): Signed[K, Out, T] = {
    new Signed(contents, signatory)
  }
}

final case class Copy[+K <: N, +T <: O[K, T]](outs: Seq[O[K, T]]) extends O[K, T]
final case class Match[+K <: N, +T <: O[K, T]](patterns: Seq[O[K, T]]) extends O[K, T]
