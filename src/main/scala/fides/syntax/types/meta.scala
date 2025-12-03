package fides.syntax.types

type TopC = Scape[TopS, TopM]

sealed trait Scape[+S <: TopS, +M <: TopM]
// todo make Scape subtyping work so that if one scape is smaller than another,
//  it's similar to how one polarity is smaller than another? Dunno
//  Or the other way around?

final abstract class EscapeC[
  S <: TopS, P <: TopP,
  ES <: Polar2[QuotedT[S], P], M <: TopM,
  +C <: Scape[ES, M],
] extends Scape[S, SomeM[P, M]]
// todo Level

final abstract class PairC[
  T1 <: TopT, T2 <: TopT, P <: TopP,
  S1 <: Polar2[T1, P], S2 <: Polar2[T2, P], M <: TopM,
  +C1 <: Scape[S1, M], +C2 <: Scape[S2, M],
] extends Scape[PairS[T1, T2, P, S1, S2], M]
