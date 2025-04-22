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
trait ValTop private[syntax]()
// todo

type ValBot = Nothing

type OffTop = Any
type OffBot = Nothing

/**
  * Fides code type for processes.
  */
trait Process extends CodeType, Code[Process]

/**
  * [[Polar]] is a generalization of expressions and patterns.
  */
trait Polar[+P >: ValBot, -N <: ValTop] extends CodeType, Code[Polar[P, N]]

/**
  * Fides code type for Fides value literals
  */
trait Lit extends CodeType

/**
  * Fides code type for expressions. While expressions are really just a special type of process with a single output,
  * they behave differently from a syntactic point of view, as [where their only output goes] is not represented
  * explicitly by a name, but implicitly by where they are written, as is usual with expressions in other languages.
  * This syntactic behavior can be viewed as some kind of mandatory syntactic sugar.
  *
  * Dual of Xctr
  */
type Expr[+T <: ValTop] = Polar[T, OffBot]

/**
  * Fides code type for extractors (aka patterns). While extractors are really just a special type of
  * process with a single input, they behave differently from a syntactic point of view, as [where their only input
  * comes from] is not represented explicitly by a name, but implicitly by where they are written, dually to
  * expressions. They can be thought of as expressions that are being evaluated backwards, with the syntax for input and
  * output being flipped.
  *
  * Dual of Expr
  */
type Xctr[-T <: ValTop] = Polar[OffTop, T]

type Ntrl[T <: ValTop] = Polar[T, T]

//trait InpLit[+T <: ValTop] extends Expr[T], Lit
//trait OutLit[-T <: ValTop] extends Xctr[T], Lit
//
//trait Lit[T <: ValTop] extends InpLit[T], OutLit[T]

/**
  * Data type for atomic values. Atomic values
  *  - can be tested for equality.
  *  - cannot be decomposed.
  */
trait Atom extends ValTop

/**
  * Unused so far. Could be used to keep track of the quote context with an additional Code parameter.
  */
//sealed trait QuoteContext private[syntax]()
//object QuoteContext:
//  sealed trait Neutral extends QuoteContext
//  sealed trait InQuote[C <: QuoteContext, P <: Polarity] extends QuoteContext
//  sealed trait Macro[I <: Int] extends QuoteContext
//end QuoteContext

/**
  * Could be used to type Escape once QuoteContext is used.
  */
//type EscapeQuoteContext[C <: QuoteContext] <: QuoteContext = C match
//  case QuoteContext.Neutral => QuoteContext.Macro[0]
//  case QuoteContext.InQuote[c, p] => c
//  case QuoteContext.Macro[i] => QuoteContext.Macro[i + 1]
