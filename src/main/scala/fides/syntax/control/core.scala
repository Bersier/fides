package fides.syntax.control

import fides.syntax.types.*

case object Kill extends OldCode[NtrlS[KillD]]
case object Pause extends OldCode[NtrlS[PauseD]]
case object Start extends OldCode[NtrlS[StartD]]

/**
  * A pausable process
  *
  * @param awake indicates the current state of the process (whether it's awake or asleep),
  *              is like a mutable variable declaration // todo no scope; more like a loc, but with different semantics?
  * @param body the process that can be paused
  */
final case class Pausable(awake: OldCode[NameS[BoolD]], body: OldCode[AplrS]) extends OldCode[AplrS]

/**
   * Delays [[body]] until [[signal]] is received.
   *
   * Executes the body only if the received signal is [[True]].
   * If [[False]], the body is discarded without getting executed.
  */
final case class Contingent(signal: OldCode[ExprS[BoolD]], body: OldCode[AplrS]) extends OldCode[AplrS]

/**
  * A killable process
  *
  * Upon reception of a pulse, the body's execution is stopped.
  *
  * @param killSignal upon reception of a pulse, the process is stopped
  * @param deathSignal upon termination (whether induced by body completion or a kill signal), a pulse is emitted
  * @param body the process that can be stopped
  */
final case class Mortal(
  killSignal: OldCode[ExprS[PulseD]],
  deathSignal: OldCode[XctrS[PulseD]],
  body: OldCode[AplrS],
) extends OldCode[AplrS]

/**
  * No message can reach and/or exit the sandboxed process directly. The monitor serves as the intermediate.
  * It can send and receive messages to and from the sandboxed process.
  */
final case class Sandboxed(monitor: OldCode[AplrS], sandboxed: OldCode[AplrS]) extends OldCode[AplrS]

final case class Handled(
  errorHandler: OldCode[CnstS[ChanD[OffTopD, ErrorD[TopD]]]],
  body: OldCode[AplrS],
) extends OldCode[AplrS]

final case class Error[D <: TopD](value: OldCode[CnstS[D]]) extends OldCode[CnstS[ErrorD[D]]]
// todo develop
