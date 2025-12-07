package fides.syntax.machinery

type TrimmedR[G <: TopG, M <: ConsM[G, TopQ], TM <: ConsM[G, ConsQ[TopP, BotQ]]] = TrimmedMR[G, TopN.`1`, M, TM]

sealed trait TrimmedMR[G <: TopG, H <: TopN, M <: ConsM[G, TopQ], TM <: ConsM[G, TopQ]]
// todo not sure if we should keep the type parameter G; we could also add Q and TQ <: Q
object TrimmedMR:
  given [
    G <: TopG, P <: TopP, Q <: TopQ, HTQ <: Q,
    ITM <: ConsM[G, ConsQ[P, BotQ]],
    H <: TopN,
    M <: ConsM[G, ConsQ[P, Q]],
    TM <: ConsM[G, ConsQ[P, HTQ]],
  ] => (
    TrimmedR[G, M, ITM],
    TrimmedMR[G, TopN.S[H], M, TM],
    // Note that TrimmedR[G, HTM, TM] should also hold, as a consequence of the two requirements above.
  ) => TrimmedMR[
    QuoteG[G, P, ITM], H,
    QuoteM[G, P, ITM, Q, M],
    QuoteM[G, P, ITM, HTQ, TM],
  ]
  given [
    D1 <: TopD, D2 <: TopD, P1 <: TopP, P2 <: TopP,
    G1 <: PolarG[D1, P1], G2 <: PolarG[D2, P2],
    H1 <: TopP, H2 <: TopP, Q1 <: TopQ, Q2 <: TopQ, TQ1 <: Q1, TQ2 <: Q2,
    H <: TopN,
    M1 <: ConsM[G1, ConsQ[H1, Q1]], M2 <: ConsM[G2, ConsQ[H2, Q2]],
    TM1 <: ConsM[G1, ConsQ[H1, TQ1]], TM2 <: ConsM[G2, ConsQ[H2, TQ2]],
  ] => (TrimmedMR[G1, H, M1, TM1], TrimmedMR[G2, H, M2, TM2]) => TrimmedMR[
    PairG[D1, D2, P1, P2, G1, G2], H,
    PairM[D1, D2, P1, P2, G1, G2, ConsQ[H1, Q1],  ConsQ[H2, Q2], M1, M2],
    PairM[D1, D2, P1, P2, G1, G2, ConsQ[H1, TQ1], ConsQ[H2, TQ2], TM1, TM2],
  ]
  // The rule below is admissible (and probably unhelpful for actual inference).
  // given [
  //   G <: TopG, H <: TopN, M <: ConsM[G, TopQ], TM <: ConsM[G, TopQ], TTM <: ConsM[G, TopQ],
  // ] => (TrimmedMR[G, TopN.S[H], M, TM], TrimmedMR[G, H, TM, TTM]) => TrimmedMR[G, H, M, TTM]
end TrimmedMR

sealed trait TrimmedQR[Height <: TopN, Q <: TopQ, TQ <: Q]
object TrimmedQR:
  given [Q <: TopQ] => TrimmedQR[TopN.Z, Q, BotQ]
  given [
    H <: TopN, P <: TopP, Q <: TopQ, TQ <: Q,
  ] => TrimmedQR[H, Q, TQ] => TrimmedQR[TopN.S[H], ConsQ[P, Q], ConsQ[P, TQ]]
end TrimmedQR
