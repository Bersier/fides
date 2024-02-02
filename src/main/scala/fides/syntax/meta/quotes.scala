package fides.syntax.meta

import fides.syntax.code.{Code, CodeType, Expr, Ptrn, Val, ValType}
import fides.syntax.values.Integer

// todo could escaped Quoted of vals be automatially unquoted? Currently, we auto-quote ValQs, but not the other way
//  around. This leads to confusing asymmetric code. We should either have both, or none.
// todo could an Xctr be in a position where it could fail in a pattern? It shouldn't, right? For example, in a
//  MatchQuote, an Escape could contain an Out(chanA), where there is no way for the type-checker to know that the
//  passed value will not fit the type expected by chanA...

/**
  * Code as value, used for metaprogramming
  */
trait Quoted[+S <: CodeType] private[syntax]() extends Val[Quoted[S]], ValType:
  def code: Code[S]
object Quoted:
  def apply[S <: CodeType](`$code`: Code[S]): Quoted[S] = new Quoted[S]():
    val code: Code[S] = `$code`
    override def toString: String = s"Quoted($code)"
end Quoted

/**
  * Allows escaping the body of a [[Quote]]. Ignores nested [[Quote]]s
  * (as well as nested [[Quoted]] and [[MatchQuote]]). Directly escapes the whole quote.
  *
  * Doesn't have a special meaning within [[MatchQuote]]s.
  *
  * (At the top-level (outside of a quote), could represent macro code.)
  */
final case class Escape[S <: CodeType](code: Code[Expr[Quoted[S]]]) extends Code[S]

/**
  * Represents an [[Escape]] within a (nested) [[Quote]], so it is simply treated like code of an [[Escape]],
  * without actually escaping.
  *
  * Upon launching of the [[Quoted]], [[QuotedEscape]] of level zero is converted to an [[Escape]].
  * Higher-level [[QuotedEscape]]s have their level lowered by one.
  */
final case class QuotedEscape[S <: CodeType](
  code: Code[Expr[Quoted[S]]],
  level: Code[Val[Integer]] = Integer(0),
) extends Code[S]

/**
  * Allows escaping the body of a [[MatchQuote]]. Ignores nested [[MatchQuote]]s
  * (as well as nested [[Quoted]] and [[Quote]]).
  *
  * Doesn't have a special meaning within [[Quote]]s.
  */
final case class MatchEscape[S <: CodeType](code: Code[Ptrn[Quoted[S], Quoted[S]]]) extends Code[S]

/**
  * Allows matching a [[MatchEscape]](Matcher) within a [[MatchQuote]]. See also [[SignedMatcher]].
  */
final case class MatchEscapeMatcher[S <: CodeType](
  code: Code[Ptrn[Quoted[S], Quoted[S]]],
  level: Code[Val[Integer]] = Integer(0),
) extends Code[S]
// todo EscapeMatcher could be merged with Escape (which would then have an additional level parameter)
//  (and similarly for other matchers)

/**
  * Analoguous to s-Strings in Scala, but for code
  *
  * Once all the Escape inside [[code]] have been evaluated and spliced in, reduces to a [[Quoted]].
  */
final case class Quote[S <: CodeType](code: Code[S]) extends Expr[Quoted[S]]

/**
  * Code extractor.
  */
final case class MatchQuote[S <: CodeType](code: Code[S]) extends Code[Ptrn[Quoted[S], ValType]]
