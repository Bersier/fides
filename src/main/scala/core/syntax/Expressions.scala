package core.syntax

trait X_TOP extends L_TOP
trait X[+K <: N, +C <: D, +T <: X_TOP] extends X_TOP with L[K, C, T]

final case class AsValue[+K <: N, T <: X[K, D, T]](value: Loc[K, T]) extends X[K, Val, Loc[K, T]]

final case class APair[+K <: N, +C <: D, +T1 <: X[K, C, T1], +T2 <: X[K, C, T2]]
(first: T1, second: T2) extends X[K, C, APair[K, C, T1, T2]]

// Have set as well (useful in building code)? (need way to match it, though (?))

final case class Signed[+K <: N, +C <: D, +T <: X[K, C, T]] private(contents: T)(signatory: X[K, C, Signatory])
  extends X[K, C, Signed[K, C, T]] {
  def this(message: T, signatoryKey: X[K, Inp, SignatoryKey]) = {
    this(message)(signatoryKey.asInstanceOf[X[K, C, Signatory]])
  }
}
object Signed {
  def out[K <: N, T <: O[K, T]](contents: T, signatory: O[K, Signatory]): Signed[K, Out, T] = {
    new Signed(contents)(signatory)
  }
}

final case class Forget[+K <: N](expr: I[K, X_TOP]) extends I[K, U]
final case class Tegrof[+K <: N](expr: O[K, U]) extends O[K, Nothing]

final case class Copy[+K <: N, +T <: O[K, T]](outs: Seq[O[K, T]]) extends O[K, T]
final case class Match[+K <: N, +T <: O[K, T]](patterns: Seq[O[K, T]]) extends O[K, T]
