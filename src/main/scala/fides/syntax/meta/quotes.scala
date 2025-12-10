package fides.syntax.meta

import fides.syntax.machinery.*

/**
  * Analogous to s-Strings in Scala, but for code-as-value, for metaprogramming
  */
final case class Quote[
  K <: TopK, P <: TopP, T2M <: ConsM[TopG], T1M <: ConsM[TopG],
  KM <: ConsM[NameG[K]], M <: ConsM[TopG],
](name: Code[KM], code: Code[M])(
  using Any, // todo should fix T1M and T2M
) extends Code[QuoteM[K, P, T2M, T1M, KM, M]]

/**
  * Allows escaping the body of a [[Quote]].
  */
final case class Escape[
  TG <: TopG,
  K <: TopK, TM <: ConsHM[TG],
  KM <: ConsM[NameG[K]], M <: ConsM[PolarG[QuoteD[TM], TopP]],
](name: Code[KM], quote: Code[M]) extends Code[EscapeM[TG, K, TM, KM, M]]
