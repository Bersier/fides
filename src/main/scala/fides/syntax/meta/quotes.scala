package fides.syntax.meta

import fides.syntax.types.*
import fides.syntax.values.Nat
import typelevelnumbers.binary.Bits
import util.TopB

/**
  * Analogous to s-Strings in Scala, but for code-as-value, for metaprogramming
  */
final case class Quote[
  S <: TopS, P <: TopP, M <: TopM,
  +C <: Code[S, SomeM[P, M]], +RC <: Code[S, SomeM[P, BotM]],
](code: C)(using TrimmedR[S, C, RC]) extends Code[QuoteS[S, P, RC], M]

// todo Only keep Quote and Escape.
// todo how to match escape (chunks) generically?
// todo update documentation

/**
  * Allows escaping the body of a [[Quote]]. Ignores nested [[Quote]]s
  * (as well as nested [[Quoted]] and [[MatchQuote]]). Directly escapes the whole quote.
  *
  * Doesn't have a special meaning within [[MatchQuote]]s.
  *
  * [[Escape]] never needs to be matched against, as it cannot appear in a [[Quoted]].
  *
  * To quote an [[Escape]] from a nested quote, rather than escaping the top-level quote, use [[QuotedEscape]].
  *
  * (At the top-level (outside of a quote), could represent macro code.)
  */
sealed trait Escape[S <: TopS, M <: TopM] extends Code[S, M]
object Escape:
  final case class Top[
    S <: TopS, P <: TopP, M <: TopM,
    +C <: Code[Polar2[QuoteT[S], P], M],
  ](code: C) extends Escape[S, SomeM[P | Polarity[TopB.T, TopB.T, TopB.F], M]]

  final case class Step[
    S <: TopS, M <: TopM,
    +C <: Escape[S, M],
  ](escape: C) extends Escape[S, SomeM[BotP, M]]
end Escape

/**
  * Represents an [[Escape]] within a (nested) [[Quote]], so it is simply treated like code of an [[Escape]],
  * without actually escaping.
  *
  * Upon launching of the [[Quoted]], [[QuotedEscape]] of level zero is converted to an [[Escape]].
  * Higher-level [[QuotedEscape]]s have their level lowered by one.
  *
  * Except perhaps for macro use, [[level]] should be smaller than quote nesting depth,
  * ensuring that the [[QuotedEscape]] gets converted to an [[Escape]] before the quote actually gets executed.
  *
  * When [[QuotedEscape]] is used within [[MatchQuote]], nothing special happens.
  * So [[QuotedEscape]]s within quotes are matched by [[QuotedEscape]]s within [[MatchQuote]], as expected.
  */
final case class QuotedEscape[S <: TopS, B <: Bits, M >: BotM <: TopM](
  // todo add lower bounds such as for M everywhere?
  code: Code[Expr2[QuotedT[S]], M],
  level: Code[Ntrl2[NatT[B]], M] = Nat(Bits.None),
) extends Code[S, SomeM[Polarity[TopB.T, TopB.F, TopB.F], M]]
// todo is SomeM's first type argument correct?

/**
  * Allows escaping the body of a [[MatchQuote]]. Ignores nested [[MatchQuote]]s
  * (as well as nested [[Quoted]] and [[Quote]]).
  *
  * Doesn't have a special meaning within [[Quote]]s.
  *
  * To quote a [[MatchEscape]] from a nested [[MatchQuote]], rather than escaping the top-level [[MatchEscape]],
  * use [[MatchEscapeMatcher]].
  */
final case class MatchEscape[S <: U, U <: TopS, M <: TopM](code: Code[Xctr2[QuotedT[U]], M]) extends Code[S, ?]
// todo add type weakening primitives instead of U
// todo set second type argument
// todo MatchEscapeS

/**
  * Allows matching a [[MatchEscape]](Matcher) within a [[MatchQuote]]. See also [[SignedMatcher]].
  *
  * Since [[MatchEscape]] escapes [[MatchQuote]], a new type of code is needed to match a [[MatchEscape]].
  * And to match that new type of code, yet a different one is needed, and so forth.
  * This is solved by having a level, effectively introducing a hierarchy of matchers.
  * This is similar to having to use a backslash in a regex to escape another backslash
  * (except that the number of backslashes needed grows exponentially with how meta the regex is).
  *
  * [[MatchEscapeMatcher]]`(c, 0)` matches [[MatchEscape]]`(c)`.
  * For `level > 0`, [[MatchEscapeMatcher]]`(c, level)` matches [[MatchEscapeMatcher]]`(c, level - 1)`.
  */
final case class MatchEscapeMatcher[S <: TopS, B <: Bits, M >: BotM <: TopM](
  code: Code[Xctr[QuotedT[S]], M],
  level: Code[Ntrl2[NatT[B]], M] = Nat(Bits.None),
) extends Code[S, ?] // todo set second type argument
