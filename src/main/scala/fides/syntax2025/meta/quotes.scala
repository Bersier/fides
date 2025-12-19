package fides.syntax2025.meta

import fides.syntax2025.machinery.*

/**
  * Analogous to s-Strings in Scala, but for code-as-value, for metaprogramming
  */
final case class Quote[
  K <: TopK, P <: TopP, T2M <: TopM, T1M <: TopM,
  KM <: GenHM[NameG[K]], M <: TopM,
](name: Code[KM], code: Code[M])(
  using Any, // todo should derive T1M and T2M
) extends Code[QuoteM[K, P, T2M, T1M, KM, M]]

/**
  * Allows escaping the body of a [[Quote]].
  */
final case class Escape[
  TG <: TopG,
  TM <: GenHM[TG],
  KM <: GenHM[NameG[TopK]], M <: GenM[PolarG[QuoteD[TM], TopP]],
](name: Code[KM], quote: Code[M]) extends Code[EscapeM[TG, TM, KM, M]]
