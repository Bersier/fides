package fides.syntax.control

import fides.syntax.code.{Atom, Code, Expr, Process, Val, ValQ, ValType, Xctr}
import fides.syntax.identifiers.{Cell, OutChan}
import fides.syntax.values.{Bool, Pulse}

sealed trait Order extends Atom, ValQ[Order]
case object Kill extends Order
case object Pause extends Order
case object Start extends Order

/**
  * A pausable process
  *
  * @param awake indicates the current state of the process (whether it's awake or asleep)
  * @param body the process that can be paused
  */
final case class Pausable(awake: Code[Cell[Bool]], body: Code[Process]) extends Process

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

final case class Handled(errorHandler: Code[Val[OutChan[Error[ValType]]]], body: Code[Process]) extends Process

final case class Error[+T <: ValType](value: Code[Val[T]]) extends ValQ[Error[T]], ValType
// todo develop
