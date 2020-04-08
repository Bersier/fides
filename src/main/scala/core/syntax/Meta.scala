package core.syntax

// Do I now need to feed S to P as well?!
// Also, does having S == A guarantee that the process is well-built? Not sure...
final case class Code[S <: G](process: Lex[CodeK[S]]) extends E[AllK[S]]

final case class Escape(expr: E[RegularK[_]]) extends E[CodeK[G]]

/**
  * Matches Escape when level is zero. Otherwise, matches Epacse of a level lower by one.
  */
final case class Epacse[S <: G](level: BigInt, expr: E[PattK[S]]) extends E[PattK[S]]


// Need dual of this as well, right? (Can use inherited abstract class for implementation)
final case class OutVar[K <: N, T <: V[K]](group: Address[K], outLoc: Out[K, T]) extends
  E[K] with
  P[PattK] with
  Loc[PattK, Nothing] // with ...

// It looks like we could actually move back to a flow syntax. Perhaps do that later once it's stable, as a form that
// directly includes semantics.
