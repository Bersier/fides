package core.syntax

import scala.annotation.unchecked.uncheckedVariance

final case class APair[+K <: N, +S <: Sort, +D[_ <: A] <: Dir[_], T1 <: A, T2 <: A](
  first: L[K, S, D[T1]],
  second: L[K, S, D[T2]],
) extends A with L[K, S#R, D[APair[_, _, _, T1, T2]]]

/**
  * Depending on C, can denote an expression, or processes to be run concurrently.
  */
final case class Bag[+K <: N, +S <: Sort, +D[_ <: A] <: Dir[_], T <: A](elements: L[K, S, D[T]]*)
  extends A with L[K, S#R, D[Bag[_, S @uncheckedVariance, Dir, T]]]

final case class Signed[+K <: N, +S <: Sort, +D[_ <: A] <: Dir[_], T <: A] private(
  contents: L[K, S, D[T]],
  signatory: L[K, S, D[SignatoryVal]],
) extends A with L[K, S#R, D[Signed[_, _, Dir, T]]]

object Signed {
  def out[K <: N, S <: Sort, T <: A](
    contents: L[K, S, Out[T]],
    signatory: L[K, S, Out[SignatoryVal]],
  ): Signed[K, S, Out, T] = {
    new Signed(contents, signatory)
  }

  def inp[K <: N, S <: Sort, T <: A](
    contents: L[K, S, Inp[T]],
    signatory: L[K, S, Inp[SignatoryKey]],
  ): Signed[K, S, Inp, T] = {
    new Signed(contents, signatory: L[K, S, Inp[SignatoryVal]])
  }
}
