package fides.syntax.control

import fides.syntax.code.{Atom, Code, Expr, Process, Val, ValQ, ValType, Xctr}
import fides.syntax.identifiers.{InpChan, OutChan}
import fides.syntax.meta.Quoted
import fides.syntax.values.Pulse

sealed trait Order extends Atom, ValQ[Order]
case object Kill extends Order
case object Pause extends Order
case object Start extends Order

/**
  * A controllable process that is awake
  *
  * A controllable process can be paused, (re)started, and killed.
  */
final case class Awake(leash: Code[InpChan[Order]], body: Code[Process]) extends Process
// todo shouldn't there be a way to know when an order is completed?

/**
  * A controllable process that is asleep
  *
  * A controllable process can be paused, (re)started, and killed.
  */
final case class Asleep(leash: Code[InpChan[Order]], body: Code[Process]) extends Process

/**
  * Upon reception of a pulse, the body's execution is started.
  */
final case class OnHold(startSignal: Code[Expr[Pulse]], body: Code[Process]) extends Process

/**
  * Upon reception of a pulse, the body's execution is stopped.
  */
final case class Mortal(killSignal: Code[Expr[Pulse]], body: Code[Process]) extends Process

/**
  * No message can reach and/or exit the sandboxed process directly. The monitor serves as the intermediate.
  * It can send and receive messages to and from the sandboxed process.
  */
final case class Sandboxed(monitor: Code[Process], sandboxed: Code[Process]) extends Process

final case class Catchable
(catchSignal: Code[Expr[Pulse]], body: Code[Process], codeReader: Xctr[Quoted[Process]]) extends Process
// todo catchable that can be restarted? Observable?

final case class Handled(errorHandler: Code[OutChan[Error[ValType]]], body: Code[Process]) extends Process

final case class Error[+T <: ValType](value: Code[Val[T]]) extends ValQ[Error[T]], ValType
// todo develop

// todo interrupts? Hot-swapping? Are these cases covered with the current control primitives?
