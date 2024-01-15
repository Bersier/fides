package fides2024.syntax.components

import fides2024.syntax.code.{Code, Component, Expr, Val, ValType, VarArgs}
import fides2024.syntax.identifiers.{Channel, Identifier}
import fides2024.syntax.values.Collected

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
  * The local identifiers are really placeholders,
  * to be replaced by new identifiers upon execution/dissolution of the scope.
  *
  * @param localIdentifiers names whose meaning is only valid within this scope
  * @param body the body of the scope, in which the replacements will take place
  */
final case class Scope(localIdentifiers: Code[Val[Collected[Identifier]]], body: Code[Component]) extends Component

/**
  * Behaviorally equivalent to an infinite number of copies of the given body
  *
  * @param body the component to be repeated
  */
final case class Repeated(body: Code[Component]) extends Component

/**
  * Composes the given components concurrently.
  */
final case class Concurrent(components: Code[VarArgs[Component]]) extends Component
