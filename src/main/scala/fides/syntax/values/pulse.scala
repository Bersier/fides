package fides.syntax.values

import fides.syntax.machinery.*

/**
  * A value that doesn't carry any information beyond causality
  * (since the sending of any value occurs before its reception).
  *
  * The corresponding type is akin to the Unit type.
  */
case object Pulse extends Code[ConsM[Ntrl2G[PulseD], BotQ]]
