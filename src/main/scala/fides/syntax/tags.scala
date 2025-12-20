package fides.syntax

// -------------------------------------------------------------------------------------------------
// This file contains some helper types to track the exact identity of a Fides value at the type
// level.
// -------------------------------------------------------------------------------------------------

/**
  * Representation of names
  */
opaque type TopK = String
private[syntax] final val launcherK: TopK = "Launcher"
type LauncherK = launcherK.type
type BotK = Nothing

/**
  * Representation of natural numbers
  */
opaque type TopN = String
type BotN = Nothing
