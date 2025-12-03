package fides.syntax.types

type TopC = Scape[TopS]

sealed trait Scape[+S <: TopS]

final abstract class EscapeC[S <: TopS, +ES <: Polar2[QuotedT[S], ?], Level] extends Scape[S]

/**
  * Only [[C1]] and [[C2]] are non-auxiliary.
  */
final abstract class PairC[
  T1 <: TopT,
  T2 <: TopT,
  P <: TopP,
  S1 <: Polar2[T1, P],
  S2 <: Polar2[T2, P],
  +C1 <: Scape[S1],
  +C2 <: Scape[S2],
] extends Scape[PairS[T1, T2, P, S1, S2]]
