package core.syntax

final case class APair[+K <: N, C[_ <: A] <: X, T1 <: A, T2 <: A](first: L[K, _ <: C[T1]], second: L[K, _ <: C[T2]])
  extends A with L[K, C[APair[_, C, T1, T2]]]

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
