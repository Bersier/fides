//package fides.syntax.machinery
//
///*
// * Trimming is wrong. We want to do scape replacement instead.
// * The escapes whose heads are at the level where the replacement occurs get replaced by their bottom landscape.
// */
//
//type TrimmedR[
//  G <: TopG, P <: TopP, Q <: TopQ,
//  M <: ConsM[G, ConsQ[P, Q]], TM <: ConsM[G, ConsQ[P, BotQ]],
//] = TrimmedMR[TopN.`1`, ConsQ[P, Q], ConsQ[P, BotQ], M, TM]
//
///**
//  * `G <: `[[TopG]]`, TQ <: Q, M <: `[[ConsM]]`[G, Q], TM <: `[[ConsM]]`[G, TQ]`
//  *
//  * @tparam H unary encoding of the trim level
//  * @tparam Q polarity stack for [[M]]
//  * @tparam TQ polarity stack for [[TM]]
//  * @tparam M scape to be trimmed
//  * @tparam TM trimmed [[M]]
//  */
//sealed trait TrimmedMR[-H <: TopN, Q <: TopQ, TQ <: TopQ, M <: ConsM[TopG, Q], TM <: ConsM[TopG, TQ]]
//object TrimmedMR:
//
//  /**
//    * Inductive case to trim [[QuoteM]]
//    *
//    * Note that in this case,
//    * [[TrimmedR]]`[G, P, Q, M, ITM]` and [[TrimmedR]]`[G, P, TQ, TM, ITM]` should also hold.
//    *
//    * `TQ <: Q`
//    */
//  given [
//    G <: TopG, P <: TopP,
//    ITM <: ConsM[G, ConsQ[P, BotQ]],
//    H <: TopN, Q <: TopQ, TQ <: TopQ,
//    M <: ConsM[G, ConsQ[P, Q]],
//    TM <: ConsM[G, ConsQ[P, TQ]],
//  ] => TrimmedMR[
//    TopN.S[H], ConsQ[P, Q], ConsQ[P, TQ],
//    M,
//    TM,
//  ] => TrimmedMR[
//    H, Q, TQ,
//    QuoteM[G, P, ITM, Q, M],
//    QuoteM[G, P, ITM, TQ, TM],
//  ]
//
//  /**
//    * Base case for trimming any subtype of [[EscapeM]] (i.e. escapes):
//    * they simply vanish from the scape when fully trimmed.
//    */
//  given [
//    G <: TopG,
//    Q <: TopQ,
//    M <: EscapeM[G, Q],
//  ] => TrimmedMR[
//    TopN.Z, Q, BotQ,
//    M,
//    ConsM[G, BotQ],
//  ]
//
//  /**
//    * Inductive case for [[EscapeM]] (i.e. escape matchers)
//    *
//    * `TQ <: Q`
//    */
//  given [
//    G <: TopG,
//    H <: TopN, Q <: TopQ, TQ <: TopQ,
//  ] => TrimmedQR[H, Q, TQ] => TrimmedMR[
//    H, Q, TQ,
//    EscapeM[G, Q],
//    EscapeM[G, TQ],
//  ]
//
//  /**
//    * Inductive case for [[EscapeM.Step]]
//    *
//    * `TQ <: Q`
//    */
//  given [
//    G <: TopG,
//    H <: TopN, Q <: TopQ, TQ <: TopQ,
//    M <: EscapeM[G, Q],
//    TM <: EscapeM[G, TQ],
//  ] => TrimmedMR[H, Q, TQ, M, TM] => TrimmedMR[
//    TopN.S[H], ConsQ[BotP, Q], ConsQ[BotP, TQ],
//    EscapeM.Step[G, Q, M],
//    EscapeM.Step[G, TQ, TM],
//  ]
//
//  /**
//    * Inductive case for [[EscapeM.Head]] (i.e. an actual escape)
//    *
//    * `TQ <: Q`
//    */
//  given [
//    G <: TopG, P <: TopP,
//    H <: TopN, Q <: TopQ, TQ <: TopQ,
//    M <: ConsM[PolarG[QuoteD[G], P], Q],
//    TM <: ConsM[PolarG[QuoteD[G], P], TQ],
//  ] => TrimmedMR[H, Q, TQ, M, TM] => TrimmedMR[
//    TopN.S[H], ConsQ[P | GenP[BotB, BotB, TopB], Q], ConsQ[P | GenP[BotB, BotB, TopB], TQ],
//    EscapeM.Head[G, P, Q, M],
//    EscapeM.Head[G, P, TQ, TM],
//  ]
//
//  /**
//    * Inductive case to trim [[PairM]]
//    *
//    * `TQ1 <: Q1, TQ2 <: Q2`
//    */
//  given [
//    D1 <: TopD, D2 <: TopD, P1 <: TopP, P2 <: TopP,
//    G1 <: PolarG[D1, P1], G2 <: PolarG[D2, P2],
//    H <: TopN, Q1 <: TopQ, Q2 <: TopQ, TQ1 <: TopQ, TQ2 <: TopQ,
//    M1 <: ConsM[G1, Q1], M2 <: ConsM[G2, Q2],
//    TM1 <: ConsM[G1, TQ1], TM2 <: ConsM[G2, TQ2],
//  ] => (TrimmedMR[H, Q1, TQ1, M1, TM1], TrimmedMR[H, Q2, TQ2, M2, TM2]) => TrimmedMR[
//    H, Q1 | Q2, TQ1 | TQ2,
//    PairM[D1, D2, P1, P2, G1, G2, Q1, Q2, M1, M2],
//    PairM[D1, D2, P1, P2, G1, G2, TQ1, TQ2, TM1, TM2],
//  ]
//
//  /**
//    * The rule below is admissible (and probably harmful for actual inference).
//    * It is here only for documentation purposes.
//    *
//    * `TQ <: Q, TTQ <: TQ`
//    */
//  protected given [
//    H <: TopN, Q <: TopQ, TQ <: TopQ, TTQ <: TopQ,
//    M <: ConsM[TopG, Q], TM <: ConsM[TopG, TQ], TTM <: ConsM[TopG, TTQ],
//  ] => (TrimmedMR[TopN.S[H], Q, TQ, M, TM], TrimmedMR[H, TQ, TTQ, TM, TTM]) => TrimmedMR[H, Q, TTQ, M, TTM]
//end TrimmedMR
//
///**
//  * `TQ <: Q`
//  *
//  * @tparam H unary encoding for the trim level
//  * @tparam Q polarity stack to be trimmed
//  * @tparam TQ trimmed [[Q]]
//  */
//sealed trait TrimmedQR[H <: TopN, Q <: TopQ, TQ <: TopQ]
//object TrimmedQR:
//
//  /**
//    * Base case: when fully trimmed (to zero height), any polarity stack simply reduces to the trivial stack.
//    */
//  given [Q <: TopQ] => TrimmedQR[TopN.Z, Q, BotQ]
//
//  /**
//    * Straightforward inductive case.
//    *
//    * `TQ <: Q`
//    */
//  given [
//    H <: TopN, P <: TopP, Q <: TopQ, TQ <: TopQ,
//  ] => TrimmedQR[H, Q, TQ] => TrimmedQR[TopN.S[H], ConsQ[P, Q], ConsQ[P, TQ]]
//end TrimmedQR
