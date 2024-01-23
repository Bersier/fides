package fides.syntax.control

import fides.syntax.code.{Atom, Code, Process, Expr, Val, ValQ, ValType, Xctr}
import fides.syntax.identifiers.{Inp, Out}
import fides.syntax.meta.Quoted
import fides.syntax.values.Pulse

sealed trait Order extends Atom, ValQ[Order], ValType
case object Kill extends Order
case object Pause extends Order
case object Start extends Order

/**
  * A controllable process that is awake
  *
  * A controllable process can be paused, (re)started, and killed.
  */
final case class Awake(leash: Code[Inp[Order]], body: Code[Process]) extends Process

/**
  * A controllable process that is asleep
  *
  * A controllable process can be paused, (re)started, and killed.
  */
final case class Asleep(leash: Code[Inp[Order]], body: Code[Process]) extends Process

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
// todo catchable that can be restarted? Readable/Snapshoteable?

final case class Handled(errorHandler: Code[Out[Error[ValType]]], body: Code[Process]) extends Process

final case class Error[+T <: ValType](value: Code[Val[T]]) extends ValQ[Error[T]], ValType
// todo develop

// todo interrupts? Hot-swapping? Are these cases covered with the current control primitives?
