package fides.syntax.types

import util.TopN

/**
  * General type to represent Fides code
  */
trait Code[+S <: TopS, +M <: TopM] private[syntax]() extends OldCode[S]
type TopC = Code[TopS, TopM]

sealed trait TopM
sealed trait SomeM[+H <: TopP, +T <: TopM] extends TopM
final abstract class BotM extends SomeM[BotP, BotM]
