package fides.syntax.types

import fides.syntax.types.TopS

import scala.language.experimental.pureFunctions

/**
  * General type to represent Fides code
  */
trait Code[+S <: TopS] private[syntax]()
// todo add QuoteContext type parameter
// todo CodePtrn, CodeExpr, CodeVal

// todo
import scala.compiletime.ops.boolean.&&

sealed trait ListT
final abstract class NoneL extends ListT
final abstract class SomeL[H <: Boolean, T <: ListT] extends ListT

sealed trait CodeL[L <: ListT]

final case class MakePair[L1 <: ListT, L2 <: ListT](v1: L1, v2: L2) extends CodeL[EmPair[L1, L2]]

type EmPair[L1 <: ListT, L2 <: ListT] <: ListT = (L1, L2) match
  case (NoneL, _) => L2
  case (_, NoneL) => L1
  case (SomeL[h1, t1], SomeL[h2, t2]) => SomeL[h1 && h2, EmPair[t1, t2]]


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
