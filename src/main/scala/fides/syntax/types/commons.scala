package fides.syntax.types

import util.{BotB, TopB}

sealed class ID
case object LauncherID extends ID

sealed trait ConsQ[+H <: TopP, +D <: TopQ]
type TopQ = ConsQ[TopP, ?]
final abstract class BotQ extends ConsQ[BotP, BotQ]

/**
  * @tparam P whether a quote of this code can be used as an expression
  * @tparam N whether a quote of this code can be used as an extractor
  * @tparam M whether a quote of this code can be used as a constant
  */
final abstract class Polarity[+P <: TopB, +N <: TopB, +M <: TopB]
type TopP = Polarity[TopB, TopB, TopB]
type BotP = Polarity[BotB, BotB, BotB]
