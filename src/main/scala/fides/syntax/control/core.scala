package fides.syntax.control

import fides.syntax.core.Code
import fides.syntax.types.*

case object Kill extends Code[Ntrl[KillT]]
case object Pause extends Code[Ntrl[PauseT]]
case object Start extends Code[Ntrl[StartT]]

/**
  * A pausable process
  *
  * @param awake indicates the current state of the process (whether it's awake or asleep),
  *              is like a mutable variable declaration // todo no scope; more like a loc, but with different semantics?
  * @param body the process that can be paused
  */
final case class Pausable(awake: Code[NameS[BoolT]], body: Code[Aplr]) extends Code[Aplr]

/**
   * Delays [[body]] until [[signal]] is received.
   *
   * Executes the body only if the received signal is [[True]].
   * If [[False]], the body is discarded without getting executed.
  */
final case class Contingent(signal: Code[Expr[BoolT]], body: Code[Aplr]) extends Code[Aplr]

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
  killSignal: Code[Expr[PulseT]],
  deathSignal: Code[Xctr[PulseT]],
  body: Code[Aplr],
) extends Code[Aplr]

/**
  * No message can reach and/or exit the sandboxed process directly. The monitor serves as the intermediate.
  * It can send and receive messages to and from the sandboxed process.
  */
final case class Sandboxed(monitor: Code[Aplr], sandboxed: Code[Aplr]) extends Code[Aplr]

final case class Handled(
  errorHandler: Code[Cnst[ChanT[OffTopT, ErrorT[TopT]]]],
  body: Code[Aplr],
) extends Code[Aplr]

final case class Error[T <: TopT](value: Code[Cnst[T]]) extends Code[Ntrl[ErrorT[T]]]
// todo develop
