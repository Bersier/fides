package fides.syntax.processes

import fides.syntax.core.Code
import fides.syntax.types.{Expr, Process, Lit, ValTop}
import fides.syntax.declarations.Declaration
import fides.syntax.identifiers.OutChan
import fides.syntax.meta.Args

/**
  * Sends a value to an address.
  *
  * The value is guaranteed to arrive eventually, assuming the address exists.
  * Sending is also guaranteed to be fully private.
  *
  * @param contents the value to be sent
  * @param recipient address of the recipient
  */
final case class Send[T <: ValTop](contents: Code[Expr[T]], recipient: Code[Expr[OutChan[T]]]) extends Process

/**
  * A message in transit
  *
  * A message cannot be captured by any Inp that is on hold. If there is a possibility for an Inp to capture the
  * message in the future, it should not disappear. In other words, it waits until an Inp is ready to receive it.
  * On the other hand, if it is known that there will never be any Inp for the channel, the message should eventually
  * get garbage-collected.
  *
  * @param contents the contents of the message
  * @param recipient the address of the recipient
  */
final case class Message[T <: ValTop](contents: Code[Lit[T]], recipient: Code[Lit[OutChan[T]]]) extends Process
// todo delete?

/**
  * A name scope
  *
  * @param declarations valid within this scope
  * @param body the body of the scope, in which the names are valid
  */
final case class Scope(declarations: Code[Args[Declaration[?]]], body: Code[Process]) extends Process

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
