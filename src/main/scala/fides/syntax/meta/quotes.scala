package fides.syntax.meta

import fides.syntax.types.*
import util.TopB

/**
  * Analogous to s-Strings in Scala, but for code-as-value, for metaprogramming
  */
final case class Quote[
  S <: TopS, P <: TopP, M <: TopM,
  +C <: Code[S, SomeM[P, M]], +RC <: Code[S, SomeM[P, BotM]],
](code: C)(using TrimmedR[S, C, RC]) extends Code[QuoteS[S, P, RC], M]

// todo Only keep Quote and Escape.
// todo how to match escape (chunks) generically? With a generic Escape.Matcher. But then how to match the matcher?
// todo update documentation

/**
  * Allows escaping the body of a [[Quote]].
  */
sealed trait Escape[S <: TopS, M <: TopM] extends Code[S, M]
object Escape:
  /**
    * Escapes one quotation level.
    */
  final case class Top[
    S <: TopS, P <: TopP, M <: TopM,
    +C <: Code[Polar2[QuoteT[S], P], M],
  ](code: C) extends Escape[S, SomeM[P | Polarity[TopB.T, TopB.T, TopB.F], M]]

  /**
    * Allows its parameter to escape one more quotation level.
    */
  final case class Step[
    S <: TopS, M <: TopM,
    +C <: Escape[S, M],
  ](escape: C) extends Escape[S, SomeM[BotP, M]]
end Escape
