package fides.syntax.code

import fides.syntax.code.Polarity.{Negative, Neutral, Positive}
import fides.syntax.meta.Quoted

import scala.compiletime.ops.int.+
import scala.language.experimental.pureFunctions

/**
  * General type to represent Fides code
  */
trait Code[+S <: CodeType] private[syntax]()
// todo add QuoteContext type parameter
// todo CodePtrn, CodeExpr, CodeVal

/**
  * Parent type of all the Scala types that represent the different types of possible Fides code.
  */
trait CodeType private[syntax]()

/**
  * Parent type of all the Scala types that represent Fides value types.
  */
trait ValType private[syntax]()
// todo

/**
  * Fides code type for processes.
  */
trait Process extends CodeType, Code[Process]

sealed trait Polarity derives CanEqual
object Polarity:
  sealed trait Positive extends Polarity
  sealed trait Negative extends Polarity
  sealed trait Neutral extends Positive, Negative
end Polarity

/**
  * [[Polar]] is a generalization of expressions and patterns.
  *
  * [[Polar]]`[`[[Positive]]`, P, N]` doesn't make sense when `N` != [[ValType]].
  *
  * So `N` is meaningless when `R` == [[Positive]].
  */
trait Polar[+R <: Polarity, +P <: N, -N <: ValType] extends CodeType, Code[Polar[R, P, N]]

/**
  * Fides code type for expressions. While expressions are really just a special type of process with a single output,
  * they behave differently from a syntactic point of view, as [where their only output goes] is not represented
  * explicitly by a name, but implicitly by where they are written, as is usual with expressions in other languages.
  * This syntactic behavior can be viewed as some kind of mandatory syntactic sugar.
  *
  * Dual of Xctr
  */
type Expr[+T <: ValType] = Polar[Positive, T, ValType]

/**
  * Fides code type for refutable patterns (non-refutable patterns, Xctr, are a special case of refutable patterns).
  *
  * Patterns do behave differently when they don't have connections: they may lead to a match failure. They are not
  * just syntactic sugar for single-input processes.
  *
  * Given a value v, if its singleton type is a supertype of [[P]], this pattern will match it.
  *
  * [[P]] ensures that it's not possible to match against cases that are known statically to never match.
  *
  * @tparam P the type of the pattern, when interpreted as a value to be matched against
  * @tparam N all values of that type are allowed to be matched against this pattern
  */
type Ptrn[+P <: N, -N <: ValType] = Polar[Negative, P, N]

/**
  * Fides code type for extractors (aka non-refutable patterns). While extractors are really just a special type of
  * process with a single input, they behave differently from a syntactic point of view, as [where their only input
  * comes from] is not represented explicitly by a name, but implicitly by where they are written, dually to
  * expressions. They can be thought of as expressions that are being evaluated backwards, with the syntax for input and
  * output being flipped.
  *
  * Dual of Expr
  */
type Xctr[-T <: ValType] = Ptrn[Nothing, T]

/**
  * Fides code type for Fides values.
  *
  * [[Val]]`[T] <: `[[Expr]]`[T] & `[[Ptrn]]`[T, `[[ValType]]`]`
  *
  * @tparam T keeps track of the value type
  */
type Val[+T <: ValType] = Polar[Neutral, T, ValType]

trait Atom extends ValType

/**
  * Unused so far. Could be used to keep track of the quote context with an additional Code parameter.
  */
sealed trait QuoteContext private[syntax]()
object QuoteContext:
  sealed trait Neutral extends QuoteContext
  sealed trait InQuote[C <: QuoteContext, P <: Polarity] extends QuoteContext
  sealed trait Macro[I <: Int] extends QuoteContext
end QuoteContext

/**
  * Could be used to type Escape once QuoteContext is used.
  */
type EscapeQuoteContext[C <: QuoteContext] <: QuoteContext = C match
  case QuoteContext.Neutral => QuoteContext.Macro[0]
  case QuoteContext.InQuote[c, p] => c
  case QuoteContext.Macro[i] => QuoteContext.Macro[i + 1]

/**
  * Could be used to type Escape once QuoteContext is used.
  */
type EscapeParamType[S <: CodeType, C <: QuoteContext] = C match
  case QuoteContext.InQuote[c, p] => Polar[p, Quoted[S], Quoted[S]]
  case _ => Expr[Quoted[S]]
// todo not sure if this is still correct or meaningful
