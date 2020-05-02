package core.syntax

import scala.annotation.unchecked.uncheckedVariance

final case class Code[+K <: N, +C[+_ <: A] <: E, +C2 <: D](code: L[CodeK[K, C], C2])
  extends A with L[K, C[Code[K, C, C2]]]

/**
  * Escapes one level of code.
  */
final case class Escape[+K <: N, +C[+_ <: A] <: E, +C2 <: D](expr: L[K, C[Code[K, C, C2]]])
  extends L[CodeK[K, C], C2]

/**
  * Matches Escape when level is zero. Otherwise, matches a MatchEscape of a level lower by one.
  *
  * Note: one would need a more advanced type system than what Scala has to fully type higher-order
  * (i.e. where level > 0) MatchEscape.
  */
final case class MatchEscape[+K <: N, +C[+_ <: A] <: E, +C2 <: D](
  level: BigInt,
  expr: L[CodeK[K, Out], C[Code[CodeK[K, Out], C, C2]]]
) extends L[CodeK[CodeK[K, Out], C], C2]
// Not sure about these type parameters...

/**
  * Depending on C, can denote an expression, or processes to be run concurrently.
  */
final case class Bag[+K <: N, +T <: A, +C[+_ <: A] <: D](elements: Multiset[L[K, C[T]] @uncheckedVariance])
  extends A with L[K, C[Bag[K, T, C]]]

/*
Code[Nothing, Val[Code[Nothing, ]]](Code(Escape[](Loc[T]())))
 */

final case class Annotated[+K <: N, C <: D](l: L[K, C], annotation: L[K, Val[_]]) extends L[K, C]

// It looks like we could actually move back to a flow syntax. Perhaps do that later once it's stable, as a form that
// directly includes semantics.
