package fides.syntax.meta

import fides.syntax.types.*
import util.{BotB, TopB}

/**
  * Analogous to s-Strings in Scala, but for code-as-value, for metaprogramming
  */
final case class Quote[
  S <: TopS, P <: TopP, Q <: TopQ,
  +C <: ConsC[S, ConsQ[P, Q]], +RC <: ConsC[S, ConsQ[P, BotQ]],
](code: C)(using TrimmedR[S, C, RC]) extends ConsC[QuoteS[S, P, RC], Q]

/**
  * Allows escaping the body of a [[Quote]].
  */
sealed trait Escape[+S <: TopS, +Q <: TopQ] extends ConsC[S, Q]
object Escape:
  /**
    * Escapes one quotation level.
    */
  final case class Top[
    S <: TopS, P <: TopP, Q <: TopQ,
    +C <: ConsC[Polar2[QuoteD[S], P], Q],
  ](code: C) extends Escape[S, ConsQ[P | Polarity[BotB, BotB, TopB], Q]]

  /**
    * Allows its parameter to escape one more quotation level.
    */
  final case class Step[
    S <: TopS, Q <: TopQ,
    +C <: Escape[S, Q],
  ](escape: C) extends Escape[S, ConsQ[BotP, Q]]

  // todo how to match the matcher?
  private final case class Matcher[S <: TopS, Q <: TopQ](
    typeRepr: ConsC[S, Q],
    // todo M should be tunable as well (?), independently of typeRepr's native M
    //  have a wrapper primitive that tunes M?
  ) extends Escape[S, Q | ConsQ[Polarity[TopB, BotB, BotB], BotQ]]
  object Matcher:
    // todo this seems like a hacky way to make Matcher behave like a supertype of Step and Top
    //  (which I believe is what we want for matching purposes)
    def apply[S <: TopS, Q <: TopQ](
      typeRepr: ConsC[S, Q],
    ): Escape[S, Q | ConsQ[Polarity[TopB, BotB, BotB], BotQ]] = new Matcher(typeRepr)
  end Matcher
end Escape
