package fides.syntax.values

import fides.syntax.code.{Atom, ValQ, ValType}
import izumi.reflect.Tag

/**
  * A value that doesn't carry any information beyond causality
  * (since the sending of any value occurs before its reception).
  *
  * The corresponding type is akin to the Unit type.
  */
case object Pulse extends Atom, ValQ[Pulse]
type Pulse = Pulse.type

/**
  * Value that represents a Fides type
  */
final case class TypeVal[T <: ValType](t: Tag[T]) extends Atom, ValQ[TypeVal[T]]

/**
  * Added mainly for convenience, so annotations and error messages can be readily readable.
  */
final case class Str(value: String) extends ValQ[Str], ValType
