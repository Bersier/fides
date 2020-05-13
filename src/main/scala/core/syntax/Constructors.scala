package core.syntax

import scala.annotation.unchecked.uncheckedVariance

final case class APair[+K <: N, +S <: Sort, +D[_ <: A] <: Dir[_], T1 <: A, T2 <: A](
  first: L[K, S, D[T1]],
  second: L[K, S, D[T2]],
) extends A with L[K, S#R, D[APair[_, _, _, T1, T2]]]

/**
  * Depending on C, can denote an expression, or processes to be run concurrently.
  */
final case class Bag[+K <: N, T <: A, +C[_ <: A] <: Sort, +B <: G](elements: L[K, C[T], B]*)
  extends A with L[K, C[Bag[_, T, D, B @uncheckedVariance]], B#R]

final case class Signed[+K <: N, T <: A, +C[_ <: A] <: X, +B <: G] private(
  contents: L[K, C[T], B],
  signatory: L[K, C[SignatoryVal], B],
) extends A with L[K, C[Signed[_, T, D, _]], B#R]

object Signed {
  def out[K <: N, T <: A](contents: L[K, Out[T]], signatory: L[K, Out[SignatoryVal]]): Signed[K, T, Out, G] = {
    new Signed(contents, signatory)
  }
  def inp[K <: N, T <: A, B <: G](
    contents: L[K, Inp[T], B],
    signatory: L[K, Inp[SignatoryKey], B],
  ): Signed[K, T, Inp, B#R] = {
    new Signed(contents, signatory: L[K, Inp[SignatoryVal]])
  }
}
