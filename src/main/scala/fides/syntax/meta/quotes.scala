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

// todo how to match escape (chunks) generically? With a generic Escape.Matcher. But then how to match the matcher?

/**
  * Allows escaping the body of a [[Quote]].
  */
sealed trait Escape[+S <: TopS, +M <: TopM] extends Code[S, M]
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

  // todo doesn't work as such, because neither Step nor Top are subtypes of it
  final case class Matcher[S <: TopS, M <: TopM](
    typeRepr: Code[S, M],
    // todo M should be tunable as well (?), independently of typeRepr's native M
    //  have a wrapper primitive that tunes M?
  ) extends Escape[S, M | SomeM[Polarity[TopB.F, TopB.T, TopB.T], BotM]]
end Escape
