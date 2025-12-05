package fides.syntax.types


/**
  * Parent type of all the Scala types that represent
  * the different types of possible Fides code, including the full metaprogramming landscape
  */
trait ConsM[+S <: TopS, +Q <: TopQ] private[syntax]() // todo seal
type TopM = ConsM[TopS, TopQ]

sealed trait ConsQ[+H <: TopP, +D <: TopQ]
type TopQ = ConsQ[TopP, ?]
final abstract class BotQ extends ConsQ[BotP, BotQ]
