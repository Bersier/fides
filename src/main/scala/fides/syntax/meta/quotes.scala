package fides.syntax.meta

import fides.syntax.types.*
import util.{BotB, TopB}

/**
  * Analogous to s-Strings in Scala, but for code-as-value, for metaprogramming
  */
final case class Quote[
  S <: TopS, P <: TopP, Q <: TopQ,
  RM <: ConsM[S, ConsQ[P, BotQ]],
  M <: ConsM[S, ConsQ[P, Q]],
](code: Code[M])(using TrimmedR[S, M, RM]) extends Code[QuoteM[S, P, Q, RM, M]]

/**
  * Allows escaping the body of a [[Quote]].
  */
object Escape:
  /**
    * Escapes one quotation level.
    */
  final case class Head[
    S <: TopS, P <: TopP, Q <: TopQ,
    M <: ConsM[Polar2S[QuoteD[S], P], Q],
  ](quote: Code[M]) extends Code[EscapeM.Head[S, P, Q, M]]

  /**
    * Allows its parameter to escape one more quotation level.
    */
  final case class Step[
    S <: TopS, Q <: TopQ,
    M <: EscapeM[S, Q],
  ](escape: Code[M]) extends Code[EscapeM.Step[S, Q, M]]

  final case class Matcher[S <: TopS, Q <: TopQ](
    typeRepr: Code[ConsM[S, Q]],
    // todo M should be tunable as well (?), independently of typeRepr's native M
    //  have a wrapper primitive that tunes M?
  ) extends Code[EscapeM[S, Q | ConsQ[Polarity[TopB, BotB, BotB], BotQ]]]
end Escape
