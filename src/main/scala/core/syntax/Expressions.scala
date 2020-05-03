package core.syntax

final case class AsValue[+K <: N, +T <: A, +C <: Loc[T]](loc: L[K, C]) extends A with L[K, Val[AsValue[K, T, C]]]

final case class APair[+K <: N, +C[+_ <: A] <: E, +T1 <: A, +T2 <: A, +C1 <: C[T1], +C2 <: C[T2]]
(first: L[K, C1], second: L[K, C2]) extends A with L[K, C[APair[K, C, T1, T2, C1, C2]]]

final case class Merge[+K <: N, +T <: A](one: L[K, Inp[Bag[K, T, Inp]]], two: L[K, Inp[Bag[K, T, Inp]]])
  extends L[K, Inp[Bag[K, T, Inp]]]

final case class Contains[+K <: N, +T <: A](bag: L[K, Inp[Bag[K, T, Inp]]], element: L[K, Inp[T]])
  extends L[K, Inp[Bag[K, T, Inp]]]

final case class Equals[+K <: N, +T <: A](one: L[K, Inp[T]], two: L[K, Inp[T]]) extends L[K, Inp[BoolVal]]

final case class Branch[+K <: N, +T <: A](one: L[K, Out[T]], two: L[K, Out[T]]) extends L[K, Out[BoolVal]]

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

final case class ForgetInp[+K <: N](expr: L[K, Inp[_]]) extends L[K, Inp[U]]
final case class ForgetOut[+K <: N](expr: L[K, Out[U]]) extends L[K, Out[Nothing]]

final case class Copy[+K <: N, +T <: A](outs: L[K, Val[Bag[K, T, Out]]]) extends L[K, Out[T]]
