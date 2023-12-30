package fides2024.syntax

/**
  * Sends a value to an address.
  *
  * @param message the value to be sent
  * @param recipient address of the recipient
  */
final case class Send(message: Code[Expr[ValType]], recipient: Code[Expr[Identifier]]) extends Component

/**
  * A message in transit
  *
  * @param message the contents of the message
  * @param recipient the address of the recipient
  * @tparam T the type of the message
  */
final case class Message[T <: ValType](message: Code[Val[T]], recipient: Code[Val[Identifier]]) extends Component

/**
  * A name scope
  *
  * @param localNames names whose meaning is only valid within this scope
  * @param body body of the scope
  */
final case class Scope(localNames: Code[Val[Collection[Identifier]]], body: Code[Component]) extends Component

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
