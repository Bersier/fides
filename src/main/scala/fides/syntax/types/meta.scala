package fides.syntax.types

/**
  * Parent type of all the Scala types that represent
  * the different types of possible Fides code, including the full metaprogramming landscape
  */
trait ConsM[+S <: TopS, +Q <: TopQ] private[syntax]() // todo seal
type TopM = ConsM[TopS, TopQ]

final abstract class QuoteM[
  S <: TopS, P <: TopP, Q <: TopQ,
  RM <: ConsM[S, ConsQ[P, BotQ]],
  +M <: ConsM[S, ConsQ[P, Q]],
] extends ConsM[QuoteS[S, P, RM], Q]

sealed trait EscapeM[+S <: TopS, +Q <: TopQ] extends ConsM[S, Q]
object EscapeM:
  final abstract class Head[
    S <: TopS, P <: TopP, Q <: TopQ,
    +M <: ConsM[Polar2S[QuoteD[S], P], Q],
  ] extends EscapeM[S, ConsQ[P | Polarity[BotB, BotB, TopB], Q]]

  final abstract class Step[
    S <: TopS, Q <: TopQ,
    +M <: EscapeM[S, Q],
  ] extends EscapeM[S, ConsQ[BotP, Q]]
end EscapeM

final abstract class PairM[
  D1 <: TopD, D2 <: TopD, P <: TopP,
  S1 <: Polar2S[D1, P], S2 <: Polar2S[D2, P], Q <: TopQ,
  +M1 <: ConsM[S1, Q], +M2 <: ConsM[S2, Q],
] extends ConsM[PairS[D1, D2, P, S1, S2], Q]
