package fides.syntax.meta

import fides.syntax.core.Code
import fides.syntax.types.{Cnst, Expr, NaturalNumberT, Ntrl, QuotedT, TopS, Xctr}
import fides.syntax.values.NaturalNumber

/**
  * Code as value, for metaprogramming
  */
final case class Quoted[S <: TopS](code: Code[S]) extends Code[Ntrl[QuotedT[S]]]

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
final case class Escape[S <: TopS](code: Code[Expr[QuotedT[S]]]) extends Code[S]

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
final case class QuotedEscape[S <: TopS](
  code: Code[Expr[QuotedT[S]]],
  level: Code[Cnst[NaturalNumberT]] = NaturalNumber(0),
) extends Code[S]

/**
  * Allows escaping the body of a [[MatchQuote]]. Ignores nested [[MatchQuote]]s
  * (as well as nested [[Quoted]] and [[Quote]]).
  *
  * Doesn't have a special meaning within [[Quote]]s.
  *
  * To quote a [[MatchEscape]] from a nested [[MatchQuote]], rather than escaping the top-level [[MatchEscape]],
  * use [[MatchEscapeMatcher]].
  */
final case class MatchEscape[S <: TopS](code: Code[Xctr[QuotedT[S]]]) extends Code[S]
// todo lossy type

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
final case class MatchEscapeMatcher[S <: TopS](
  code: Code[Xctr[QuotedT[S]]],
  level: Code[Cnst[NaturalNumberT]] = NaturalNumber(0),
) extends Code[S]

/**
  * Analogous to s-Strings in Scala, but for code
  *
  * Once all the [[Escape]]s inside [[code]] have been evaluated and spliced in, reduces to a [[Quoted]].
  */
final case class Quote[S <: TopS](code: Code[S]) extends Code[Expr[QuotedT[S]]]

/**
  * Code extractor.
  */
final case class MatchQuote[S <: TopS](code: Code[S]) extends Code[Xctr[QuotedT[S]]]
// todo lossy type
