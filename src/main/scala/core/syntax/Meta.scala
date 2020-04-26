package core.syntax

final case class Code[+K <: N, +C[+_] <: Exp[_], +C2 <: D](code: L[CodeK[K, C], C2])
  extends ValT with L[K, C[Code[K, C, C2]]]

/**
  * Escapes one level of code.
  */
final case class Escape[+K <: N, +C[+_] <: Exp[_], +C2 <: D](expr: L[K, C[Code[K, C, C2]]])
  extends L[CodeK[K, C], C2]

/**
  * Matches Escape when level is zero. Otherwise, matches a MatchEscape of a level lower by one.
  */
final case class MatchEscape[+K <: N, +C[+_] <: Exp[_], +C2 <: D](
  level: BigInt,
  expr: L[CodeK[K, Out], C[Code[CodeK[K, Out], C, C2]]]
) extends L[CodeK[CodeK[K, Out], C], C2]
// Not sure about these type parameters...

// It looks like we could actually move back to a flow syntax. Perhaps do that later once it's stable, as a form that
// directly includes semantics.

/*
Code[Nothing, Val[Code[Nothing, ]]](Code(Escape[](Loc[T]())))
 */
