package core.syntax

final case class AsValue[K <: N, T <: A, C <: Loc[T]](loc: L[K, C]) extends A with L[K, Val[AsValue[K, T, C]]]

final case class APair[+K <: N, +C[+_ <: A] <: X, +T1 <: A, +T2 <: A, +C1 <: C[T1], +C2 <: C[T2]]
(first: L[K, C1], second: L[K, C2]) extends A with L[K, C[APair[K, C, T1, T2, C1, C2]]]

final case class Signed[+K <: N, T <: A, C[_ <: A] <: X] private(
  contents: L[K, C[T]],
  signatory: L[K, C[SignatoryVal]]
) extends A with L[K, C[Signed[_, T, C]]]
object Signed {
  def out[K <: N, T <: A](contents: L[K, Out[T]], signatory: L[K, Out[SignatoryVal]]): Signed[K, T, Out] = {
    new Signed(contents, signatory)
  }
  def inp[K <: N, T <: A]
    (contents: L[K, Inp[T]], signatory: L[K, Inp[SignatoryKey]]): Signed[K, T, Inp] = {
    new Signed(contents, signatory: L[K, Inp[SignatoryVal]])
  }
}
