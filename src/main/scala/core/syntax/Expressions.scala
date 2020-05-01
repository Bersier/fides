package core.syntax

final case class AsValue[+K <: N, +T <: ValT](loc: L[K, Loc[T]]) extends L[K, Val[LocVal[T]]]

final case class APair[+K <: N, +C[+_] <: D, +T1 <: ValT, +T2 <: ValT, +C1 <: C[T1], +C2 <: C[T2]]
(first: L[K, C1], second: L[K, C2]) extends ValT with L[K, C[APair[K, C, T1, T2, C1, C2]]]

final case class ASet[+K <: N, T <: ValT, C[+_] <: D](elements: Multiset[L[K, C[T]]])
  extends ValT with L[K, C[ASet[K, C, T]]]
// Can't use this set type for Concurrent... Make processes expressions; again?

final case class Merge[+K <: N, T <: ValT, C[+_] <: D](
  one: L[K, C[ASet[K, T, C]]],
  two: L[K, C[ASet[K, T, C]]],
) extends L[K, C[ASet[K, T, C]]]

// Have set as well (useful in building code)? (need way to match it, though (?))

final case class Signed[+K <: N, +T <: ValT, +C[+_] <: D] private(
  contents: L[K, C[T]],
  signatory: L[K, C[SignatoryVal]]
) extends ValT with L[K, C[Signed[K, T, C]]]

object Signed {
  def out[K <: N, T <: ValT](contents: L[K, Out[T]], signatory: L[K, Out[SignatoryVal]]): Signed[K, T, Out] = {
    new Signed(contents, signatory)
  }
  def inp[K <: N, T <: ValT, C[+_] <: Inp[_]]
  (contents: L[K, C[T]], signatory: L[K, C[SignatoryKey]]): Signed[K, T, C] = {
    new Signed(contents, signatory)
  }
}

final case class ForgetInp[+K <: N](expr: L[K, Inp[_]]) extends L[K, Inp[U]]
final case class ForgetOut[+K <: N](expr: L[K, Out[U]]) extends L[K, Out[Nothing]]

final case class Copy[+K <: N, +C <: Out[_]](outs: Multiset[L[K, C]]) extends L[K, C]
final case class Match[+K <: N, +C <: Out[_]](patterns: Seq[L[K, C]]) extends L[K, C]
