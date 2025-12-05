package fides.syntax.types

type TrimmedR[G <: TopG, M <: ConsM[G, TopQ], RM <: ConsM[G, ConsQ[TopP, BotQ]]] = TrimmedGR[G, TopN.`1`, M, RM]

sealed trait TrimmedGR[G <: TopG, Height <: TopN, M <: ConsM[G, TopQ], RM <: ConsM[G, ConsQ[TopP, BotQ]]]
// todo not sure if we should keep the type parameter G
object TrimmedGR:
  given [
    D1 <: TopD, D2 <: TopD, P <: TopP,
    G1 <: Polar2G[D1, P], G2 <: Polar2G[D2, P], H <: TopP, Q <: TopQ,
    Height <: TopN, M1 <: ConsM[G1, ConsQ[H, Q]], M2 <: ConsM[G2, ConsQ[H, Q]],
    RM1 <: ConsM[G1, ConsQ[H, BotQ]], RM2 <: ConsM[G2, ConsQ[H, BotQ]],
  ] => (TrimmedGR[G1, Height, M1, RM1], TrimmedGR[G2, Height, M2, RM2]) => TrimmedGR[
    PairG[D1, D2, P, G1, G2], Height,
    PairM[D1, D2, P, G1, G2, ConsQ[H, Q], M1, M2],
    PairM[D1, D2, P, G1, G2, ConsQ[H, BotQ], RM1, RM2],
  ]
end TrimmedGR
