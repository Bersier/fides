package fides.syntax.control

import fides.syntax.core.Code
import fides.syntax.types.{ChanT, ErrorT, Expr, KillT, Lit, Ntrl, OffTop, PauseT, Process, PulseT, StartT, ValTop, Xctr}

case object Kill extends Code[Lit & Ntrl[KillT]]
case object Pause extends Code[Lit & Ntrl[PauseT]]
case object Start extends Code[Lit & Ntrl[StartT]]

/**
  * A pausable process
  *
  * @param awake indicates the current state of the process (whether it's awake or asleep)
  * @param body the process that can be paused
  */
//final case class Pausable(awake: Code[Cell[BoolT]], body: Code[Process]) extends Process

/**
  * Upon reception of a pulse, the body's execution is started.
  */
final case class OnHold(startSignal: Code[Expr[PulseT]], body: Code[Process]) extends Code[Process]

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
  body: Code[Process],
) extends Code[Process]

/**
  * No message can reach and/or exit the sandboxed process directly. The monitor serves as the intermediate.
  * It can send and receive messages to and from the sandboxed process.
  */
final case class Sandboxed(monitor: Code[Process], sandboxed: Code[Process]) extends Code[Process]

final case class Handled(
  errorHandler: Code[Expr[ChanT[OffTop, ErrorT[ValTop]]] & Lit],
  body: Code[Process],
) extends Code[Process]

final case class Error[T <: ValTop](value: Code[Lit & Expr[T]]) extends Code[Lit & Ntrl[ErrorT[T]]]
// todo develop
