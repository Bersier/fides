package fides.syntax.meta

import fides.syntax.machinery.*

/**
  * Analogous to s-Strings in Scala, but for code-as-value, for metaprogramming
  */
final case class Quote[
  G <: TopG, P <: TopP,
  TM <: ConsM[G, ConsQ[P, BotQ]], Q <: TopQ,
  M <: ConsM[G, ConsQ[P, Q]],
](code: Code[M])(using TrimmedR[G, P, Q, M, TM]) extends Code[QuoteM[G, P, TM, Q, M]]

/**
  * Allows escaping the body of a [[Quote]].
  */
object Escape:
  /**
    * Escapes one quotation level.
    */
  final case class Head[
    G <: TopG, P <: TopP, Q <: TopQ,
    M <: ConsM[PolarG[QuoteD[G], P], Q],
  ](quote: Code[M]) extends Code[EscapeM.Head[G, P, Q, M]]

  /**
    * Allows its parameter to escape one more quotation level.
    */
  final case class Step[
    G <: TopG, Q <: TopQ,
    M <: EscapeM[G, Q],
  ](escape: Code[M]) extends Code[EscapeM.Step[G, Q, M]]

  final case class Matcher[G <: TopG, Q <: TopQ](
    typeRepr: Code[ConsM[G, Q]],
    // todo M should be tunable as well (?), independently of typeRepr's native M
    //  have a wrapper primitive that tunes M?
  ) extends Code[EscapeM[G, Q | ConsQ[GenP[TopB, BotB, BotB], BotQ]]]
end Escape
