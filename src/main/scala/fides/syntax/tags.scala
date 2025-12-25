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
type BotK = Nothing

type KInv[K >: BotK <: TopK] <: TopK = K match
  case Nothing => String
  // todo...

/**
  * Representation of natural numbers
  */
opaque type TopN = String
type BotN = Nothing
