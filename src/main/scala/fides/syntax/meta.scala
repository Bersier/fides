package fides.syntax

// -------------------------------------------------------------------------------------------------
// This file contains the constructors to build a full characterization of any piece of Fides code,
// including top-level escapes.
// -------------------------------------------------------------------------------------------------

sealed trait TopM private[syntax]()
final abstract class BotM extends TopM
