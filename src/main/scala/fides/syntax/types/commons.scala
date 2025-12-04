package fides.syntax.types

import util.TopB

sealed class ID
case object LauncherID extends ID

/**
  * @tparam P whether a quote of this code can be used as an expression
  * @tparam N whether a quote of this code can be used as an extractor
  * @tparam C whether a quote of this code can be used as a constant
  */
final abstract class Polarity[+P <: TopB, +N <: TopB, +C <: TopB]
type TopP = Polarity[TopB.F, TopB.F, TopB.F]
type BotP = Polarity[TopB.T, TopB.T, TopB.T]

trait OldCode[+S <: TopS] private[syntax]()
