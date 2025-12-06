package fides.syntax.control

import fides.syntax.machinery.*

case object Kill extends OldCode[NtrlG[KillD]]
case object Pause extends OldCode[NtrlG[PauseD]]
case object Start extends OldCode[NtrlG[StartD]]

/**
  * A pausable process
  *
  * @param awake indicates the current state of the process (whether it's awake or asleep),
  *              is like a mutable variable declaration // todo no scope; more like a loc, but with different semantics?
  * @param body the process that can be paused
  */
final case class Pausable(awake: OldCode[NameG[BoolD]], body: OldCode[AplrG]) extends OldCode[AplrG]

/**
   * Delays [[body]] until [[signal]] is received.
   *
   * Executes the body only if the received signal is [[True]].
   * If [[False]], the body is discarded without getting executed.
  */
final case class Contingent(signal: OldCode[ExprG[BoolD]], body: OldCode[AplrG]) extends OldCode[AplrG]

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
  killSignal: OldCode[ExprG[PulseD]],
  deathSignal: OldCode[XctrG[PulseD]],
  body: OldCode[AplrG],
) extends OldCode[AplrG]

/**
  * No message can reach and/or exit the sandboxed process directly. The monitor serves as the intermediate.
  * It can send and receive messages to and from the sandboxed process.
  */
final case class Sandboxed(monitor: OldCode[AplrG], sandboxed: OldCode[AplrG]) extends OldCode[AplrG]

final case class Handled(
  errorHandler: OldCode[CnstG[ChanD[OffTopD, ErrorD[TopD]]]],
  body: OldCode[AplrG],
) extends OldCode[AplrG]

final case class Error[D <: TopD](value: OldCode[CnstG[D]]) extends OldCode[CnstG[ErrorD[D]]]
// todo develop
