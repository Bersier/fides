package fides.syntax.values

import fides.syntax.code.{Atom, Lit, ValTop}

/**
  * A value that doesn't carry any information beyond causality
  * (since the sending of any value occurs before its reception).
  *
  * The corresponding type is akin to the Unit type.
  */
case object Pulse extends Atom, Lit[Pulse]
type Pulse = Pulse.type

/**
  * Added mainly for convenience, so annotations and error messages can be readily readable.
  */
final case class Str(value: String) extends Lit[Str], ValTop
