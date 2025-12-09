package fides.syntax.machinery

// todo I believe we have the property that at height H, the P in TQ is BotP; double-check and document

/**
  * Reduction type-evaluates the escapes that end one level above [[H]].
  *
  * @tparam H unary encoding of the level at which the reduction is to occur
  * @tparam Q polarity stack for [[M]]
  * @tparam TQ polarity stack for [[TM]]
  * @tparam M scape to be reduced at [[H]]
  * @tparam TM reduced [[M]]
  */
sealed trait MReductionR[-H <: TopN, Q <: TopQ, TQ <: TopQ, M <: ConsM[TopG, Q], TM <: ConsM[TopG, TQ]]
object MReductionR:

  /**
    * Inductive case for [[QuoteM]]
    */
  given [
    P <: TopP, `P'` <: TopP, ITM <: TopM, TITM <: TopM,
    H <: TopN, Q <: TopQ, TQ <: TopQ,
    M <: ConsM[TopG, ConsQ[P, Q]],
    TM <: ConsM[TopG, ConsQ[`P'`, TQ]],
  ] => MReductionR[
    TopN.S[H], ConsQ[P, Q], ConsQ[`P'`, TQ],
    M,
    TM,
  ] => MReductionR[
    H, Q, TQ,
    QuoteM[P, Q, ITM, M],
    QuoteM[`P'`, TQ, TITM, TM],
  ]

  /**
    * Base case for [[EscapeM.Head]] when `H` is zero
    */
  given [
    TG <: TopG, TQ <: TopQ,
    TM <: ConsHM[TG, TQ], P <: TopP, Q <: TopQ, // todo P and Q are loose
    M <: ConsM[PolarG[QuoteD[TM], P], Q],
  ] => MReductionR[
    TopN.Z, ConsQ[P | BotVP, Q], TQ,
    EscapeM.Head[TG, TM, P, Q, M],
    TM,
  ]

  /**
    * Base case for [[EscapeM.Step]] when `H` is zero
    */
  given [
  // todo Q is very loose. But maybe that's okay? Maybe as long as the relation is a superset of the required one,
  //  and is tight for the Ms, it's okay?
    Q <: TopQ,
    M <: EscapeM.Step[? <: TopG, Q, ?, ?, ?],
  ] => MReductionR[TopN.Z, ConsQ[BotP, Q], ConsQ[BotP, Q], M, M]
  // todo remove ` <: TopG`?

  /**
    * Base case for escape matchers when `H` is zero
    */
  given [TG <: TopG, Q <: TopQ, TM <: ConsHM[TG, TopQ], M <: TopM] => MReductionR[
    TopN.Z, Q, Q & ConsQ[BotP, TopQ],
    EscapeM[TG, Q, TM, M],
    ConsM[TG, Q & ConsQ[BotP, TopQ]],
  ]

  /**
    * Inductive case for [[EscapeM.Head]] (when `H` is non-zero)
    */
  given [
    ITG <: TopG, `ITG'` <: TopG,
    ITM <: ConsHM[ITG, TopQ], `ITM'` <: ConsHM[`ITG'`, TopQ], P <: TopP, `P'` <: TopP,
    H <: TopN, Q <: TopQ, TQ <: TopQ, // todo `P'` <: P is not enforced
    M <: ConsM[PolarG[QuoteD[ITM], P], Q],
    TM <: ConsM[PolarG[QuoteD[`ITM'`], `P'`], TQ],
  ] => MReductionR[H, Q, TQ, M, TM] => MReductionR[
    TopN.S[H], ConsQ[P | BotVP, Q], ConsQ[`P'` | BotVP, TQ],
    EscapeM.Head[ITG, ITM, P, Q, M],
    EscapeM.Head[`ITG'`, `ITM'`, `P'`, TQ, TM],
  ]

  /**
    * Inductive case for [[EscapeM.Step]] (when `H` is non-zero)
    */
  given [
    H <: TopN,
    ITG <: TopG, `ITG'` <: TopG,
    Q <: TopQ, EITM <: ConsHM[ITG, TopQ], EM <: TopM, TQ <: TopQ, `EITM'` <: ConsHM[`ITG'`, TopQ], TEM <: TopM,
    M <: EscapeHM[ITG, Q, EITM, EM],
    TM <: EscapeHM[`ITG'`, TQ, `EITM'`, TEM],
  ] => MReductionR[H, Q, TQ, M, TM] => MReductionR[
    TopN.S[H], ConsQ[BotP, Q], ConsQ[BotP, TQ],
    EscapeM.Step[ITG, Q, EITM, EM, M],
    EscapeM.Step[`ITG'`, TQ, `EITM'`, TEM, TM],
  ]

  /**
    * Inductive case for escape matchers (when `H` is non-zero)
    */
  given [H <: TopN, TG <: TopG, Q <: TopQ, TM <: ConsHM[TG, TopQ], M <: TopM] => MReductionR[
    TopN.S[H], Q, Q,
    EscapeM[TG, Q, TM, M],
    EscapeM[TG, Q, TM, M],
  ]

  /**
    * Inductive case for [[PairM]]
    */
  given [
    D1 <: TopD, D2 <: TopD, P1 <: TopP, P2 <: TopP,
    G1 <: PolarG[D1, P1], G2 <: PolarG[D2, P2],
    H <: TopN, Q1 <: TopQ, Q2 <: TopQ, TQ1 <: TopQ, TQ2 <: TopQ,
    M1 <: ConsHM[G1, Q1], M2 <: ConsHM[G2, Q2],
    TM1 <: ConsHM[G1, TQ1], TM2 <: ConsHM[G2, TQ2],
  ] => (MReductionR[H, Q1, TQ1, M1, TM1], MReductionR[H, Q2, TQ2, M2, TM2]) => MReductionR[
    H, Q1 | Q2, TQ1 | TQ2,
    PairM[D1, D2, P1, P2, G1, G2, Q1, Q2, M1, M2],
    PairM[D1, D2, P1, P2, G1, G2, TQ1, TQ2, TM1, TM2],
  ]
end MReductionR

/**
  * `TQ <: Q`
  *
  * @tparam H unary encoding for the trim level
  * @tparam Q polarity stack to be trimmed
  * @tparam TQ trimmed [[Q]]
  */
sealed trait QTrimmingR[H <: TopN, Q <: TopQ, TQ <: TopQ]
object QTrimmingR:

  /**
    * Base case: when fully trimmed (to zero height), any polarity stack simply reduces to the trivial stack.
    */
  given [Q <: TopQ] => QTrimmingR[TopN.Z, Q, BotQ]

  /**
    * Straightforward inductive case.
    *
    * `TQ <: Q`
    */
  given [
    H <: TopN, P <: TopP, Q <: TopQ, TQ <: TopQ,
  ] => QTrimmingR[H, Q, TQ] => QTrimmingR[TopN.S[H], ConsQ[P, Q], ConsQ[P, TQ]]
end QTrimmingR
