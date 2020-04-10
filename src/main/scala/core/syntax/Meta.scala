package core.syntax

// Not sure about the Cs in here...

final case class Code[+K <: N, +C <: D, +C2 <: D, +T <: Lex[K, C, T]]
(lex: Lex[CodeK[K, C], C2, T]) extends X[K, C, Code[K, C, C2, T]]

final case class Escape[+K <: N, +C <: D, +T <: X[K, C, T]](expr: X[K, C, T]) extends X[CodeK[K, C], Val, T]
// Need level for this, or need to repeat it, to escape code within code...

/**
  * Matches Escape when level is zero. Otherwise, matches Epacse of a level lower by one.
  */
final case class Epacse[S <: G](level: BigInt, expr: E[PattK[S]]) extends E[PattK[S]]

// It looks like we could actually move back to a flow syntax. Perhaps do that later once it's stable, as a form that
// directly includes semantics.
