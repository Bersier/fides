package fides.syntax.connectors

import fides.syntax.code.{Code, Expr, Process, ValType, Xctr}
import fides.syntax.meta.Args
import fides.syntax.values.Pulse

/**
  * A hard-coded connection between one input and one output
  *
  * Equivalent to [[Spread]]([[inp]], [[Args]]([[out]])).
  */
final case class Forward[T <: ValType](inp: Code[Expr[T]], out: Code[Xctr[T]]) extends Process

/**
  * Kind-of the dual of values.
  *
  * Aka Sink, Forget
  *
  * Can be implemented in terms of the other primitives.
  */
final case class Ignore() extends Xctr[ValType]

/**
  * Spreads a value to multiple recipients.
  */
final case class Spread[T <: ValType](value: Code[Expr[T]], recipients: Code[Args[Xctr[T]]]) extends Process

/**
  * Forwards the inputted value once signalled to do so.
  */
final case class Hold[T <: ValType](signal: Code[Expr[Pulse]], value: Code[Expr[T]]) extends Expr[T]

/**
  * Upon reception of a value, outputs a pulse. It only communicates the arrival of the value,
  * but forgets/ignores about the actual value.
  */
final case class Signal(trigger: Code[Expr[?]]) extends Expr[Pulse]

/**
  * Forwards one of the inputs. It is guaranteed to forward a value if any of the inputs yields a value.
  *
  * Another way to think about this is that it forwards the value of the expression that "first" reduces to a value.
  */
final case class Pick[T <: ValType](inputs: Code[Args.Some[Expr[T]]]) extends Expr[T]

/**
  * Internal choice. Non-deterministically forwards the input to one of the outputs.
  */
final case class UnPick[T <: ValType](recipients: Code[Args.Some[Xctr[T]]]) extends Xctr[T]
