package fides.syntax.code

import fides.syntax.meta.Quoted

import compiletime.ops.int.+
import language.experimental.pureFunctions

/**
  * General type to represent Fides code
  */
trait Code[+S <: CodeType] private[syntax]()

/**
  * Parent type of all the Scala types that represent the different types of possible Fides code.
  */
trait CodeType private[syntax]()

/**
  * Parent type of all the Scala types that represent Fides value types.
  */
sealed trait ValType private[syntax]()

/**
  * Fides code type for processes.
  *
  * For convenience, Process also extends Code[Process], so that we can write
  * "Foo extends Process", rather than "Foo extends Process, Code[Process]".
  */
trait Process extends CodeType, Code[Process]

/**
  * Fides code type for expressions. While expressions are really just a special type of process with a single output,
  * they behave differently from a syntactic point of view, as [where their only output goes] is not represented
  * explicitly by a name, but implicitly by where they are written, as is usual with expressions in other languages.
  * This syntactic behavior could be viewed as some kind of mandatory syntactic sugar.
  *
  * Dual of Xctr
  *
  * For convenience, Expr[T] also extends Code[Expr[T]], so that we can write
  * "Foo extends Expr[Foo]", rather than "Foo extends Expr[Foo], Code[Expr[Foo]]".
  */
trait Expr[+T <: ValType] extends CodeType, Code[Expr[T]]
// todo replace Expr and Ptrn by Polr[Polarity, +T <: ValType, +P <: N, -N <: ValType]?

// todo improve documentation
/**
  * Patterns do behave differently when they don't have connections: they may lead to a match failure. They are not
  * just syntactic sugar for single-input processes.
  *
  * Refutable patterns (includes non-refutable ones as a special case)
  */
trait Ptrn[+P <: N, -N <: ValType] extends CodeType, Code[Ptrn[P, N]]

/**
  * Fides code type for extractors. While extractors are really just a special type of process with a single input,
  * they behave differently from a syntactic point of view, as [where their only input comes from] is not represented
  * explicitly by a name, but implicitly by where they are written, dually to expressions. They can be thought of as
  * expressions that are being evaluated backwards, with the syntax for input and output being flipped.
  *
  * Non-refutable patterns
  *
  * Dual of Expr
  *
  * For convenience, Xctr[T] also extends Code[Xctr[T]], so that we can write
  * "Foo extends Xctr[Foo]", rather than "Foo extends Xctr[Foo], Code[Xctr[Foo]]".
  */
type Xctr[-T <: ValType] = Ptrn[Nothing, T]

/**
  * Fides code type for Fides values.
  *
  * For convenience, Val[T] also extends Code[Val[T]] and ValType, so that we can write
  * "Foo extends Val[Foo]", rather than "Foo extends Val[Foo], Code[Val[Foo]], ValType".
  *
  * @tparam T keeps track of the value type
  */
trait Val[+T <: ValType] extends Expr[T], Ptrn[T, ValType], Code[Val[T]], ValType
// todo maybe we should remove these conveniences, as they allow nonsensical types like Val[Val[?]]...

/**
  * This is a tentative flattening of (nested) quotes of values.
  */
trait ValQ[+T <: ValType] extends Val[T], Code[Quoted[Val[T]]]
// todo doesn't seem to work in the test...

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
