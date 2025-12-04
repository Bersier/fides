package fides.syntax.types

import util.TopN

/**
  * General type to represent Fides code
  */
trait Code[+S <: TopS, +Q <: TopQ] private[syntax]() extends OldCode[S]
type TopC = Code[TopS, TopQ]

sealed trait TopQ
sealed trait SomeQ[+H <: TopP, +T <: TopQ] extends TopQ
final abstract class BotQ extends SomeQ[BotP, BotQ]
