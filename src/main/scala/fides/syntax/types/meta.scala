package fides.syntax.types

// todo use
trait Code[C <: TopC] private[syntax]()

/**
  * General type to represent Fides code
  */
trait ConsC[+S <: TopS, +Q <: TopQ] private[syntax]() // todo seal
type TopC = ConsC[TopS, TopQ]

sealed trait ConsQ[+H <: TopP, +T <: TopQ]
type TopQ = ConsQ[TopP, ?]
final abstract class BotQ extends ConsQ[BotP, BotQ]
