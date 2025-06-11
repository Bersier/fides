package fides.syntax.core

import fides.syntax.types.CodeType

import scala.language.experimental.pureFunctions

/**
  * General type to represent Fides code
  */
trait Code[+S <: CodeType] private[syntax]()
// todo add QuoteContext type parameter
// todo CodePtrn, CodeExpr, CodeVal

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
