package core.syntax

import scala.annotation.unchecked.uncheckedVariance

final case class APair[+K <: N, +C[_ <: A] <: X, T1 <: A, T2 <: A, +B <: G](
  first: M[K, C[T1], B],
  second: M[K, C[T2], B],
) extends A with M[K, C[APair[_, D, T1, T2, _]], B#R]

/**
  * Depending on C, can denote an expression, or processes to be run concurrently.
  */
final case class Bag[+K <: N, T <: A, +C[_ <: A] <: Sort, +B <: G](elements: M[K, C[T], B]*)
  extends A with M[K, C[Bag[_, T, D, B @uncheckedVariance]], B#R]

final case class Signed[+K <: N, T <: A, +C[_ <: A] <: X, +B <: G] private(
  contents: M[K, C[T], B],
  signatory: M[K, C[SignatoryVal], B],
) extends A with M[K, C[Signed[_, T, D, _]], B#R]

object Signed {
  def out[K <: N, T <: A](contents: L[K, Out[T]], signatory: L[K, Out[SignatoryVal]]): Signed[K, T, Out, G] = {
    new Signed(contents, signatory)
  }
  def inp[K <: N, T <: A, B <: G](
    contents: M[K, Inp[T], B],
    signatory: M[K, Inp[SignatoryKey], B],
  ): Signed[K, T, Inp, B#R] = {
    new Signed(contents, signatory: L[K, Inp[SignatoryVal]])
  }
}
