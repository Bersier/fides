package fides.syntax.meta

import fides.syntax.types.*
import fides.syntax.values.Nat
import typelevelnumbers.binary.Bits

/**
  * Analogous to s-Strings in Scala, but for code-as-value, for metaprogramming
  */
final case class Quote[S <: TopS, P <: TopP, M <: TopM](
  code: Code2[S, SomeM[P, M]],
) extends Code2[Polar2[QuotedT[S], P], M]

// todo there might be a better escape system, where Escape is more generic, rather than being specific to Expr

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
final case class Escape[S <: TopS, M <: TopM](
  code: Code2[Expr2[QuotedT[S]], M],
) extends Code2[S, SomeM[Polarity[Bool.T, Bool.F, Bool.F], M]]

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
  code: Code2[Expr2[QuotedT[S]], M],
  level: Code2[Ntrl2[NatT[B]], M] = Nat(Bits.None),
) extends Code2[S, SomeM[Polarity[Bool.T, Bool.F, Bool.F], M]]
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
final case class MatchEscape[S <: U, U <: TopS, M <: TopM](code: Code2[Xctr2[QuotedT[U]], M]) extends Code2[S, ?]
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
  code: Code2[Xctr[QuotedT[S]], M],
  level: Code2[Ntrl2[NatT[B]], M] = Nat(Bits.None),
) extends Code2[S, ?] // todo set second type argument
