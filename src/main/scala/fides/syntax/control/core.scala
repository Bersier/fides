package fides.syntax.control

import fides.syntax.types.*

case object Kill extends OldCode[Ntrl[KillD]]
case object Pause extends OldCode[Ntrl[PauseD]]
case object Start extends OldCode[Ntrl[StartD]]

/**
  * A pausable process
  *
  * @param awake indicates the current state of the process (whether it's awake or asleep),
  *              is like a mutable variable declaration // todo no scope; more like a loc, but with different semantics?
  * @param body the process that can be paused
  */
final case class Pausable(awake: OldCode[NameS[BoolD]], body: OldCode[Aplr]) extends OldCode[Aplr]

/**
   * Delays [[body]] until [[signal]] is received.
   *
   * Executes the body only if the received signal is [[True]].
   * If [[False]], the body is discarded without getting executed.
  */
final case class Contingent(signal: OldCode[Expr[BoolD]], body: OldCode[Aplr]) extends OldCode[Aplr]

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
  killSignal: OldCode[Expr[PulseD]],
  deathSignal: OldCode[Xctr[PulseD]],
  body: OldCode[Aplr],
) extends OldCode[Aplr]

/**
  * No message can reach and/or exit the sandboxed process directly. The monitor serves as the intermediate.
  * It can send and receive messages to and from the sandboxed process.
  */
final case class Sandboxed(monitor: OldCode[Aplr], sandboxed: OldCode[Aplr]) extends OldCode[Aplr]

final case class Handled(
  errorHandler: OldCode[Cnst[ChanD[OffTopD, ErrorD[TopD]]]],
  body: OldCode[Aplr],
) extends OldCode[Aplr]

final case class Error[D <: TopD](value: OldCode[Cnst[D]]) extends OldCode[Cnst[ErrorD[D]]]
// todo develop
