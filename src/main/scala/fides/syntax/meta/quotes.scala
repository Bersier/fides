package fides.syntax.meta

import fides.syntax.types.*
import util.TopB

/**
  * Analogous to s-Strings in Scala, but for code-as-value, for metaprogramming
  */
final case class Quote[
  S <: TopS, P <: TopP, Q <: TopQ,
  +C <: Code[S, SomeQ[P, Q]], +RC <: Code[S, SomeQ[P, BotQ]],
](code: C)(using TrimmedR[S, C, RC]) extends Code[QuoteS[S, P, RC], Q]

/**
  * Allows escaping the body of a [[Quote]].
  */
sealed trait Escape[+S <: TopS, +Q <: TopQ] extends Code[S, Q]
object Escape:
  /**
    * Escapes one quotation level.
    */
  final case class Top[
    S <: TopS, P <: TopP, Q <: TopQ,
    +C <: Code[Polar2[QuoteT[S], P], Q],
  ](code: C) extends Escape[S, SomeQ[P | Polarity[TopB.T, TopB.T, TopB.F], Q]]

  /**
    * Allows its parameter to escape one more quotation level.
    */
  final case class Step[
    S <: TopS, Q <: TopQ,
    +C <: Escape[S, Q],
  ](escape: C) extends Escape[S, SomeQ[BotP, Q]]

  // todo how to match the matcher?
  private final case class Matcher[S <: TopS, Q <: TopQ](
    typeRepr: Code[S, Q],
    // todo M should be tunable as well (?), independently of typeRepr's native M
    //  have a wrapper primitive that tunes M?
  ) extends Escape[S, Q | SomeQ[Polarity[TopB.F, TopB.T, TopB.T], BotQ]]
  object Matcher:
    // todo this seems like a hacky way to make Matcher behave like a supertype of Step and Top
    //  (which I believe is what we want for matching purposes)
    def apply[S <: TopS, Q <: TopQ](
      typeRepr: Code[S, Q],
    ): Escape[S, Q | SomeQ[Polarity[TopB.F, TopB.T, TopB.T], BotQ]] = new Matcher(typeRepr)
  end Matcher
end Escape
