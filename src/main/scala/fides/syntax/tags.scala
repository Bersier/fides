package fides.syntax

// -------------------------------------------------------------------------------------------------
// This file contains some helper types to track the exact identity of Fides values at the type
// level.
// -------------------------------------------------------------------------------------------------

/**
  * Representation of names
  */
opaque type TopK = String
private[syntax] final val launcherK: TopK = "Launcher"
type LauncherK = launcherK.type
opaque type BotK <: launcherK.type = Nothing

/**
  * Representation of natural numbers
  */
opaque type TopN = String
opaque type PosN <: TopN = String
opaque type BotN <: PosN = Nothing
