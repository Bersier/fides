package fides.syntax.meta

import fides.syntax.machinery.*

/**
  * Analogous to s-Strings in Scala, but for code-as-value, for metaprogramming
  */
final case class Quote[
  TQ <: TopQ,
  K <: TopK, KQ <: TopQ, P <: TopP, Q <: TopQ, T2M <: ConsM[TopG, TQ], T1M <: ConsM[TopG, ConsQ[P, TopQ]],
  KM <: ConsM[NameG[K], KQ], M <: ConsM[TopG, ConsQ[P, Q]],
](name: Code[KM], code: Code[M])(
  using Any, // todo should fix T1M and T2M
) extends Code[QuoteM[K, KQ, P, Q, T2M, T1M, KM, M]]

/**
  * Allows escaping the body of a [[Quote]].
  */
final case class Escape[
  TG <: TopG,
  K <: TopK, KQ <: TopQ, TM <: ConsHM[TG, TopQ], P <: TopP, Q <: TopQ,
  KM <: ConsM[NameG[K], KQ], M <: ConsM[PolarG[QuoteD[TM], P], Q],
](name: Code[KM], quote: Code[M]) extends Code[EscapeM[TG, K, KQ, TM, P, Q, KM, M]]
