package fides.syntax.processes

import fides.syntax.machinery.*

/**
  * Sends a value to an address.
  *
  * The value is guaranteed to arrive eventually, assuming the address exists.
  * Sending is also guaranteed to be fully private.
  *
  * A message cannot be captured by any Inp that is on hold. If there is a possibility for an Inp to capture the
  * message in the future, it should not disappear. In other words, a message waits until an Inp is ready to receive it.
  * On the other hand, if it is known that there will never be any Inp for the channel, the message should eventually
  * get garbage-collected.
  *
  * @param contents the value to be sent
  * @param recipient address of the recipient
  */
final case class Send[D <: TopD](
  contents: OldCode[OldExprG[D]],
  recipient: OldCode[OldExprG[ChanD[OffTopD, D]]],
) extends OldCode[AplrG]

/**
  * A name scope
  *
  * @param declarations valid within this scope
  * @param body the body of the scope, in which the names are valid
  */
final case class Scope(declarations: OldCode[ArgsUG[DeclG[?]]], body: OldCode[AplrG]) extends OldCode[AplrG]

/**
  * Behaviorally equivalent to an infinite number of copies of the given body
  *
  * @param body the process to be repeated
  */
final case class Repeated[
  G <: AplrG, Q <: TopQ,
  M <: ConsM[G, Q],
](body: Code[M]) extends Code[RepeatedM[G, Q, M]]

/**
  * Composes the given processes concurrently.
  */
final case class Concurrent[
  G <: ArgsUG[AplrG], Q <: TopQ,
  M <: ConsM[G, Q],
](processes: Code[M]) extends Code[ConcurrentM[G, Q, M]]
