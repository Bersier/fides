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
  * Inversion relation for K
  */
sealed trait KInvR[K >: BotK <: TopK, `-K` >: BotK <: TopK]
sealed trait KInvLR:
  given [K >: BotK <: TopK] => KInvR[K, K]
object KInvR extends KInvLR:
  given KInvR[BotK, TopK]
  given KInvR[TopK, BotK]
end KInvR

/**
  * Representation of natural numbers
  */
opaque type TopN = String
opaque type PosN <: TopN = String
opaque type BotN <: PosN = Nothing
