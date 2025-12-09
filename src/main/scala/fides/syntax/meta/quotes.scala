package fides.syntax.meta

import fides.syntax.machinery.*

/**
  * Analogous to s-Strings in Scala, but for code-as-value, for metaprogramming
  */
final case class Quote[
  TQ <: TopQ,
  K <: TopK, KQ <: TopQ, P <: TopP, Q <: TopQ, TM <: ConsM[TopG, TQ],
  KM <: ConsM[NameG[K], KQ], M <: ConsM[TopG, ConsQ[P, Q]],
](name: Code[KM], code: Code[M])(
  using Any, // todo MReductionR[TopN.Z, ConsQ[P, Q], TQ, M, TM],
) extends Code[QuoteM[K, KQ, P, Q, TM, KM, M]]

/**
  * Allows escaping the body of a [[Quote]].
  */
final case class Escape[
  TG <: TopG,
  K <: TopK, KQ <: TopQ, TM <: ConsHM[TG, TopQ], P <: TopP, Q <: TopQ,
  KM <: ConsM[NameG[K], KQ], M <: ConsM[PolarG[QuoteD[TM], P], Q],
](name: Code[KM], quote: Code[M]) extends Code[EscapeM[TG, K, KQ, TM, P, Q, KM, M]]
