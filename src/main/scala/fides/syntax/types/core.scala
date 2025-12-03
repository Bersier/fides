package fides.syntax.types

import fides.syntax.types.*

import scala.language.experimental.pureFunctions

/**
  * General type to represent Fides code
  */
trait Code[+S <: TopS] private[syntax]()

trait Code2[S <: TopS, +M <: TopM] extends Code[S]
type NtrlC[T <: TopT] = Code2[Ntrl2[T], BotM]

trait Code3[C <: TopC] private[syntax]()
// todo perhaps we can get rid of this wrapper entirely

sealed trait TopM
sealed trait SomeM[+H <: TopP, +T <: TopM] extends TopM
final abstract class BotM extends SomeM[BotP, BotM]

sealed trait Bool
object Bool:
  type F = Bool
  final abstract class T extends Bool
end Bool

type TopP = Polarity[Bool.F, Bool.F, Bool.F]
final abstract class Polarity[+P <: Bool, +N <: Bool, +C <: Bool]
type BotP = Polarity[Bool.T, Bool.T, Bool.T]

sealed class ID
case object LauncherID extends ID

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
