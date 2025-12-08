package fides.syntax.meta

import fides.syntax.machinery.*

/**
  * Analogous to s-Strings in Scala, but for code-as-value, for metaprogramming
  */
final case class Quote[
  P <: TopP, Q <: TopQ, TQ <: TopQ,
  TM <: ConsM[TopG, TQ],
  M <: ConsM[TopG, ConsQ[P, Q]],
](code: Code[M])(using MReductionR[TopN.Z, ConsQ[P, Q], TQ, M, TM]) extends Code[QuoteM[P, Q, TM, M]]

/**
  * Allows escaping the body of a [[Quote]].
  */
object Escape:
  /**
    * Escapes one quotation level.
    */
  final case class Head[
    TG <: TopG,
    TM <: ConsHM[TG, TopQ], P <: TopP, Q <: TopQ,
    M <: ConsM[PolarG[QuoteD[TM], P], Q],
  ](quote: Code[M]) extends Code[EscapeM.Head[TG, TM, P, Q, M]]

  /**
    * Allows its parameter to escape one more quotation level.
    */
  final case class Step[
    TG <: TopG,
    Q <: TopQ, ETM <: ConsHM[TG, TopQ], EM <: TopM,
    M <: EscapeHM[TG, Q, ETM, EM],
  ](escape: Code[M]) extends Code[EscapeM.Step[TG, Q, ETM, EM, M]]

  final case class Matcher[TG <: TopG, Q <: TopQ, TM <: ConsHM[TG, TopQ], M <: TopM](
    typeRepr: Code[ConsM[TG, Q]],
    // todo M should be tunable as well (?), independently of typeRepr's native M
    //  have a wrapper primitive that tunes M?
  ) extends Code[EscapeM[TG, Q | ConsQ[GenP[TopB, BotB, BotB], BotQ], TM, M]]
end Escape
