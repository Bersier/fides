package fides.syntax.types

type TopC = Scape[TopS, TopM]

sealed trait Scape[+S <: TopS, +M <: TopM]
// todo make Scape subtyping work so that if one scape is smaller than another,
//  it's similar to how one polarity is smaller than another? Dunno
//  Or the other way around? We'd need another level to do such stuff.

// todo get correct S from QuoteC? Probably circular... Let's code QuoteC and see

final abstract class QuoteC[
  S <: TopS, P <: TopP, M <: TopM,
  C <: Scape[S, SomeM[P, M]],
] extends Scape[QuoteS[S, P, TrimmedC[S, P, C]], M]

type Trimmed[C <: TopC] <: Scape[TopS, SomeM[TopP, BotM]] = C match
  case PairC[t1, t2, p, s1, s2, m, c1, c2] => m match
    case SomeM[p2, _] => PairC[t1, t2, p, s1, s2, SomeM[p2, BotM], Trimmed[c1], Trimmed[c2]]

final abstract class TrimmedC[+S <: TopS, +P <: TopP, +C <: Scape[S, SomeM[P, ?]]] extends Scape[S, SomeM[P, BotM]]

final abstract class EscapeC[
  S <: TopS, P <: TopP,
  ES <: Polar2[QuotedT[S], P], M <: TopM, // todo note that ES cannot just be any TopS...
  +C <: Scape[ES, M],
] extends Scape[S, SomeM[P, M]]
// todo Level

final abstract class PairC[
  T1 <: TopT, T2 <: TopT, P <: TopP,
  S1 <: Polar2[T1, P], S2 <: Polar2[T2, P], M <: TopM,
  +C1 <: Scape[S1, M], +C2 <: Scape[S2, M],
] extends Scape[PairS[T1, T2, P, S1, S2], M]
