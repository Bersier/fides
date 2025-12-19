package fides.syntax

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
