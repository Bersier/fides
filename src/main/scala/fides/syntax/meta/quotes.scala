package fides.syntax.meta

import fides.syntax.machinery.*

/**
  * Analogous to s-Strings in Scala, but for code-as-value, for metaprogramming
  */
final case class Quote[
  P <: TopP, Q <: TopQ, TM <: TopM,
  M <: ConsM[TopG, ConsQ[P, Q]],
](code: Code[M])(using MReductionR[TopN.Z, ConsQ[P, Q], ConsQ[P, BotQ], M, TM]) extends Code[QuoteM[P, Q, TM, M]]

/**
  * Allows escaping the body of a [[Quote]].
  */
object Escape:
  /**
    * Escapes one quotation level.
    */
  final case class Head[
    TG <: TopG,
    TM <: ConsM[TG, TopQ], P <: TopP, Q <: TopQ,
    M <: ConsM[PolarG[QuoteD[TM], P], Q],
  ](quote: Code[M]) extends Code[EscapeM.Head[TG, TM, P, Q, M]]

  /**
    * Allows its parameter to escape one more quotation level.
    */
  final case class Step[
    TG <: TopG,
    Q <: TopQ, ETM <: ConsM[TG, TopQ], EM <: TopM,
    M <: EscapeM[TG, Q, ETM, EM],
  ](escape: Code[M]) extends Code[EscapeM.Step[TG, Q, ETM, EM, M]]

  final case class Matcher[TG <: TopG, Q <: TopQ, TM <: ConsM[TG, TopQ], M <: TopM](
    typeRepr: Code[ConsM[TG, Q]],
    // todo M should be tunable as well (?), independently of typeRepr's native M
    //  have a wrapper primitive that tunes M?
  ) extends Code[EscapeM[TG, Q | ConsQ[GenP[TopB, BotB, BotB], BotQ], TM, M]]
end Escape
