package core.syntax

final case class U() extends A with V[U]

sealed trait BoolVal extends A with V[BoolVal]
object True extends BoolVal
object False extends BoolVal

final class Atom extends A with V[Atom]

final case class Error[+K <: N, +C <: E](value: L[K, C]) extends A with V[Error[K, C]]
// Replace with designated error Loc

final case class Z(value: BigInt) extends A with V[Z]

final case class AsValue[+K <: N, +T <: A, +C <: Loc[T]](loc: L[K, C]) extends A with L[K, Val[AsValue[K, T, C]]]

final case class APair[+K <: N, +C[+_ <: A] <: E, +T1 <: A, +T2 <: A, +C1 <: C[T1], +C2 <: C[T2]]
(first: L[K, C1], second: L[K, C2]) extends A with L[K, C[APair[K, C, T1, T2, C1, C2]]]

final case class Signed[+K <: N, +T <: A, +C[+_ <: A] <: E] private(
  contents: L[K, C[T]],
  signatory: L[K, C[SignatoryVal]]
) extends A with L[K, C[Signed[K, T, C]]]
object Signed {
  def out[K <: N, T <: A](contents: L[K, Out[T]], signatory: L[K, Out[SignatoryVal]]): Signed[K, T, Out] = {
    new Signed(contents, signatory)
  }
  def inp[K <: N, T <: A, C[+_ <: A] <: Inp[A]]
  (contents: L[K, C[T]], signatory: L[K, C[SignatoryKey]]): Signed[K, T, C] = {
    new Signed(contents, signatory)
  }
}
