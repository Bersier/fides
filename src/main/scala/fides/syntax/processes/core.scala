package fides.syntax.processes

import fides.syntax.code.{Code, Expr, Process, Val, ValType}
import fides.syntax.identifiers.{Channel, Identifier}
import fides.syntax.meta.Args

/**
  * Sends a value to an address.
  *
  * The value is guaranteed to arrive eventually, assuming the address exists.
  * Sending is also guaranteed to be fully private.
  *
  * @param message the value to be sent
  * @param recipient address of the recipient
  */
final case class Send[T <: ValType](message: Code[Expr[T]], recipient: Code[Expr[Channel[? >: T]]]) extends Process

/**
  * A message in transit
  *
  * A message cannot be captured by any Inp that is on hold. If there is a possibility for an Inp to capture the
  * message in the future, it should not disappear. In other words, it waits until an Inp is ready to receive it.
  * On the other hand, if it is known that there will never be any Inp for the channel, the message should eventually
  * get garbage-collected.
  *
  * @param message the contents of the message
  * @param recipient the address of the recipient
  */
final case class Message[T <: ValType](message: Code[Val[T]], recipient: Code[Val[Channel[T]]]) extends Process

/**
  * A name scope
  *
  * The local identifiers are really placeholders,
  * to be replaced by new identifiers upon execution/dissolution of the scope.
  *
  * @param localIdentifiers names whose meaning is only valid within this scope
  * @param body the body of the scope, in which the replacements will take place
  */
final case class Scope(localIdentifiers: Code[Args[Identifier]], body: Code[Process]) extends Process

/**
  * Behaviorally equivalent to an infinite number of copies of the given body
  *
  * @param body the process to be repeated
  */
final case class Repeated(body: Code[Process]) extends Process

/**
  * Composes the given processes concurrently.
  */
final case class Concurrent(processes: Code[Args[Process]]) extends Process
