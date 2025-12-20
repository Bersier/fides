package fides.syntax

// -------------------------------------------------------------------------------------------------
// This file contains the constructors to build a full characterization of any piece of Fides code,
// including top-level escapes.
// -------------------------------------------------------------------------------------------------

sealed trait TopM private[syntax]()
final abstract class BotM extends ReducedEscapeM[BotM, BotC]

/**
  * Special M type used to represent the "shadow" of an escape that has been reduced.
  */
sealed trait ReducedEscapeM[+DataM <: TopM, +Context <: TopC] extends TopM

sealed trait TopC
final abstract class BotC extends TopC
