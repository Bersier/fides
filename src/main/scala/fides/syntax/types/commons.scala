package fides.syntax.types

import util.{BotB, TopB}

sealed class ID
case object LauncherID extends ID

/**
  * @tparam P whether a quote of this code can be used as an expression
  * @tparam N whether a quote of this code can be used as an extractor
  * @tparam C whether a quote of this code can be used as a constant
  */
final abstract class Polarity[+P <: TopB, +N <: TopB, +C <: TopB]
type TopP = Polarity[TopB, TopB, TopB]
type BotP = Polarity[BotB, BotB, BotB]

trait OldCode[+S <: TopS] private[syntax]()
