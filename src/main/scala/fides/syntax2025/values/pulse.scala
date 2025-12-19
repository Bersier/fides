package fides.syntax2025.values

import fides.syntax2025.machinery.*

/**
  * A value that doesn't carry any information beyond causality
  * (since the sending of any value occurs before its reception).
  *
  * The corresponding type is akin to the Unit type.
  */
case object Pulse extends Code[GenM[NtrlG[PulseD]]]
