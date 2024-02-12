package fides.syntax.code

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

/**
  * Fides code type for processes.
  */
trait Process extends CodeType, Code[Process]

// todo Polar[L <: Polarity, +P <: N, -N <: ValType]
//  Is it an issue that Polar[+, P, T] doesn't make sense when T != ValType?
//  And with this style, it is not possible to type e.g. UnWrap differently from Wrap.

/**
  * Fides code type for expressions. While expressions are really just a special type of process with a single output,
  * they behave differently from a syntactic point of view, as [where their only output goes] is not represented
  * explicitly by a name, but implicitly by where they are written, as is usual with expressions in other languages.
  * This syntactic behavior can be viewed as some kind of mandatory syntactic sugar.
  *
  * Dual of Xctr
  */
trait Expr[+T <: ValType] extends CodeType, Code[Expr[T]]

/**
  * Fides code type for refutable patterns (non-refutable patterns, Xctr, are a special case of refutable patterns).
  *
  * Patterns do behave differently when they don't have connections: they may lead to a match failure. They are not
  * just syntactic sugar for single-input processes.
  *
  * Given a value v, if its singleton type is a supertype of [[P]], this pattern will match it.
  *
  * @tparam N all values of that type are allowed to be matched against this pattern
  * @tparam P the type of the pattern, when interpreted as a value to be matched against
  */
trait Ptrn[+P <: N, -N <: ValType] extends CodeType, Code[Ptrn[P, N]]

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
  * @tparam T keeps track of the value type
  */
trait Val[+T <: ValType] extends Expr[T], Ptrn[T, ValType], Code[Val[T]]

/**
  * Allows flattening of (nested) quotes of (non-quote) values.
  */
transparent trait ValQ[+T <: ValType] extends Quoted[ValQ[T]], Val[T], Code[ValQ[T]]:
  final def code: ValQ[T] = this
end ValQ

trait Atom extends ValType

/**
  * Unused so far. Could be used to keep track of the quote context with an additional Code parameter.
  */
sealed trait QuoteContext private[syntax]()
sealed trait Neutral extends QuoteContext
sealed trait InQuote[C <: QuoteContext, P <: Polarity] extends QuoteContext
sealed trait Macro[I <: Int] extends QuoteContext

enum Polarity derives CanEqual:
  case Expr, Xctr, Ptrn

/**
  * Could be used to type Escape once QuoteContext is used.
  */
type EscapeQuoteContext[C <: QuoteContext] <: QuoteContext = C match
  case Neutral => Macro[0]
  case InQuote[c, p] => c
  case Macro[i] => Macro[i + 1]

/**
  * Could be used to type Escape once QuoteContext is used.
  */
type EscapeParamType[S <: CodeType, C <: QuoteContext] = C match
  case InQuote[c, p] => p match
    case Polarity.Expr.type => Expr[Quoted[S]]
    case Polarity.Xctr.type => Xctr[Quoted[S]]
    case Polarity.Ptrn.type => Ptrn[Quoted[S], Quoted[S]]
  case _ => Expr[Quoted[S]]
