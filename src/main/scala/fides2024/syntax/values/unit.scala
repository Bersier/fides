package fides2024.syntax.values

import fides2024.syntax.code.{Atom, ValQ}

sealed trait Pulse extends Atom, ValQ[Pulse]

/**
  * A value that doesn't carry any information beyond causality
  * (since the sending of any value occurs before its reception).
  *
  * The corresponding type, Pulse, is like the Unit type in Fides.
  */
case object U extends Pulse
