package fides.syntax.values

import fides.syntax.code.{Atom, ValQ, ValType}

sealed trait Pulse extends Atom, ValQ[Pulse], ValType

/**
  * A value that doesn't carry any information beyond causality
  * (since the sending of any value occurs before its reception).
  *
  * The corresponding type, Pulse, is like the Unit type in Fides.
  */
case object U extends Pulse
