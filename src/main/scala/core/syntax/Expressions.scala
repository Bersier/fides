package core.syntax

trait TOP_X extends TOP_L
trait X[+K <: N, +C <: D, +T <: TOP_X] extends TOP_X with L[K, C, T] // Use only values in T? So TOP_V?

final case class AsValue[+K <: N, T <: TOP_X](value: Loc[K, T]) extends X[K, Val, Loc[K, T]]

final case class APair[+K <: N, +C <: D, +T1 <: X[K, C, T1], +T2 <: X[K, C, T2]]
(first: T1, second: T2) extends X[K, C, APair[K, C, T1, T2]]

final case class ASet[+K <: N, +C <: D, T <: X[K, C, T]](elements: Multiset[T]) extends X[K, C, ASet[K, C, T]]

final case class Merge[+K <: N, +C >: Inp with Out <: D, T <: X[K, C, T]](
    one: X[K, C, ASet[K, C, T]],
    two: X[K, C, ASet[K, C, T]],
) extends X[K, C, ASet[K, C, T]]

// Have set as well (useful in building code)? (need way to match it, though (?))

final case class Signed[+K <: N, +C <: D, +T <: X[K, C, T]] private(contents: T, signatory: X[K, C, Signatory])
  extends X[K, C, Signed[K, C, T]]
object Signed {
  def out[K <: N, T <: O[K, T]](contents: T, signatory: O[K, Signatory]): Signed[K, Out, T] = {
    new Signed(contents, signatory)
  }
  def inp[K <: N, T <: I[K, T]](contents: T, signatory: I[K, SignatoryKey]): Signed[K, Inp, T] = {
    new Signed(contents, signatory)
  }
}

final case class ForgetInp[+K <: N](expr: I[K, TOP_X]) extends I[K, U]
final case class ForgetOut[+K <: N](expr: O[K, U]) extends O[K, Nothing]

final case class Copy[+K <: N, +T <: O[K, T]](outs: Seq[O[K, T]]) extends O[K, T]
final case class Match[+K <: N, +T <: O[K, T]](patterns: Seq[O[K, T]]) extends O[K, T]
