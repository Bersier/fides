package fides.syntax.types

import fides.syntax.values.Pair
import util.TopN

type TrimmedR[S <: TopS, C <: ConsC[S, TopQ], RC <: ConsC[S, ConsQ[TopP, BotQ]]] = TrimmedGR[S, TopN.`1`, C, RC]

sealed trait TrimmedGR[S <: TopS, Height <: TopN, C <: ConsC[S, TopQ], RC <: ConsC[S, ConsQ[TopP, BotQ]]]
// todo not sure if we should keep the type parameter S
object TrimmedGR:
  given [
    D1 <: TopD, D2 <: TopD, P <: TopP,
    S1 <: Polar2[D1, P], S2 <: Polar2[D2, P], H <: TopP, Q <: TopQ,
    Height <: TopN, C1 <: ConsC[S1, ConsQ[H, Q]], C2 <: ConsC[S2, ConsQ[H, Q]],
    RC1 <: ConsC[S1, ConsQ[H, BotQ]], RC2 <: ConsC[S2, ConsQ[H, BotQ]],
  ] => (TrimmedGR[S1, Height, C1, RC1], TrimmedGR[S2, Height, C2, RC2]) => TrimmedGR[
    PairS[D1, D2, P, S1, S2], Height,
    Pair[D1, D2, P, S1, S2, ConsQ[H, Q], C1, C2],
    Pair[D1, D2, P, S1, S2, ConsQ[H, BotQ], RC1, RC2],
  ]
end TrimmedGR
