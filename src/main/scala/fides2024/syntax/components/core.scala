package fides2024.syntax.components

import fides2024.syntax.identifiers.{Cell, Channel}
import fides2024.syntax.kinds.{Code, Component, Expr, Val, ValType, Xctr}
import fides2024.syntax.values.Collected

/**
  * A hard-coded connection between one input and one output
  */
final case class Forward[T <: ValType](input: Code[Expr[T]], output: Code[Xctr[T]]) extends Component

/**
  * Sends a value to an address.
  *
  * The value is guaranteed to arrive eventually, assuming the address exists.
  * Sending is also guaranteed to be fully private.
  *
  * @param message the value to be sent
  * @param recipient address of the recipient
  */
final case class Send[T <: ValType](message: Code[Expr[T]], recipient: Code[Expr[Channel[T]]]) extends Component

/**
  * A message in transit
  *
  * @param message the contents of the message
  * @param recipient the address of the recipient
  */
final case class Message[T <: ValType](message: Code[Val[T]], recipient: Code[Val[Channel[T]]]) extends Component

/**
  * A name scope
  *
  * @param localChannels names whose meaning is only valid within this scope
  * @param localCells names whose meaning is only valid within this scope
  * @param body the body of the scope
  */
final case class Scope(
  localChannels: Code[Val[Collected[Channel[?]]]],
  localCells: Code[Val[Collected[Cell[?]]]],
  body: Code[Component],
) extends Component

/**
  * Behaviorally equivalent to an infinite number of copies of the given body
  *
  * @param body the component to be repeated
  */
final case class Repeated(body: Code[Component]) extends Component

/**
  * Composes the given components concurrently.
  */
final case class Concurrent(components: Code[Component]*) extends Component

// todo error handling (and interruption?)
