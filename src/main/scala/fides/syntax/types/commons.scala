package fides.syntax.types

import util.Bool

sealed class ID
case object LauncherID extends ID

/**
  * @tparam P whether a quote of this code can be used as an expression
  * @tparam N whether a quote of this code can be used as an extractor
  * @tparam C whether a quote of this code can be used as a constant
  */
final abstract class Polarity[+P <: Bool, +N <: Bool, +C <: Bool]
type TopP = Polarity[Bool.F, Bool.F, Bool.F]
type BotP = Polarity[Bool.T, Bool.T, Bool.T]

trait OldCode[+S <: TopS] private[syntax]()
