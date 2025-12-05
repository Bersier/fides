package fides.syntax.types

type TrimmedR[S <: TopS, M <: ConsM[S, TopQ], RM <: ConsM[S, ConsQ[TopP, BotQ]]] = TrimmedGR[S, TopN.`1`, M, RM]

sealed trait TrimmedGR[S <: TopS, Height <: TopN, M <: ConsM[S, TopQ], RM <: ConsM[S, ConsQ[TopP, BotQ]]]
// todo not sure if we should keep the type parameter S
object TrimmedGR:
  given [
    D1 <: TopD, D2 <: TopD, P <: TopP,
    S1 <: Polar2S[D1, P], S2 <: Polar2S[D2, P], H <: TopP, Q <: TopQ,
    Height <: TopN, M1 <: ConsM[S1, ConsQ[H, Q]], M2 <: ConsM[S2, ConsQ[H, Q]],
    RM1 <: ConsM[S1, ConsQ[H, BotQ]], RM2 <: ConsM[S2, ConsQ[H, BotQ]],
  ] => (TrimmedGR[S1, Height, M1, RM1], TrimmedGR[S2, Height, M2, RM2]) => TrimmedGR[
    PairS[D1, D2, P, S1, S2], Height,
    PairM[D1, D2, P, S1, S2, ConsQ[H, Q], M1, M2],
    PairM[D1, D2, P, S1, S2, ConsQ[H, BotQ], RM1, RM2],
  ]
end TrimmedGR
