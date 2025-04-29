package fides.syntax.control

import fides.syntax.core.Code
import fides.syntax.types.{AtomT, Expr, Process, Lit, Ntrl, ValTop, Xctr}
import fides.syntax.identifiers.OutChan
import fides.syntax.values.{BoolT, Pulse}

sealed trait OrderT extends AtomT
sealed trait KillT extends OrderT
sealed trait PauseT extends OrderT
sealed trait StartT extends OrderT

case object Kill extends Lit, Ntrl[KillT]
case object Pause extends Lit, Ntrl[PauseT]
case object Start extends Lit, Ntrl[StartT]

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
final case class OnHold(startSignal: Code[Expr[Pulse]], body: Code[Process]) extends Process

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
  killSignal: Code[Expr[Pulse]],
  deathSignal: Code[Xctr[Pulse]],
  body: Code[Process],
) extends Process

/**
  * No message can reach and/or exit the sandboxed process directly. The monitor serves as the intermediate.
  * It can send and receive messages to and from the sandboxed process.
  */
final case class Sandboxed(monitor: Code[Process], sandboxed: Code[Process]) extends Process

final case class Handled(errorHandler: Code[Expr[OutChan[Error[ValTop]]] & Lit], body: Code[Process]) extends Process

final case class Error[+T <: ValTop](value: Code[Expr[T] & Lit]) extends Lit, Ntrl[Error[T]], ValTop
// todo develop
