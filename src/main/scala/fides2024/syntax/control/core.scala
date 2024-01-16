package fides2024.syntax.control

import fides2024.syntax.code.{Atom, Code, Component, Expr, Val, ValQ, ValType, Xctr}
import fides2024.syntax.identifiers.{Inp, Out}
import fides2024.syntax.meta.Quoted
import fides2024.syntax.values.Pulse

sealed trait Order extends Atom, ValQ[Order]
case object Kill extends Order
case object Stop extends Order
case object Start extends Order

final case class Awake(leash: Code[Inp[Order]], body: Code[Component]) extends Component
final case class Asleep(leash: Code[Inp[Order]], body: Code[Component]) extends Component

/**
  * Upon reception of a pulse, the body's execution is started.
  */
final case class OnHold(startSignal: Code[Expr[Pulse]], body: Code[Component]) extends Component

/**
  * Upon reception of a pulse, the body's execution is stopped.
  */
final case class Mortal(killSignal: Code[Expr[Pulse]], body: Code[Component]) extends Component

/**
  * No message can reach and/or exit the sandboxed component directly. The monitor serves as the intermediate.
  * It can send and receive messages to and from the sandboxed component.
  */
final case class Sandboxed(monitor: Code[Component], sandboxed: Code[Component]) extends Component

final case class Catchable
(catchSignal: Code[Expr[Pulse]], body: Code[Component], codeReader: Xctr[Quoted[Component]]) extends Component
// todo catchable that can be restarted?

final case class Handled(errorHandler: Code[Out[Error[ValType]]], body: Code[Component]) extends Component

final case class Error[+T <: ValType](value: Code[Val[T]]) extends Val[Error[T]]
// todo develop

// todo interrupts? Hot-swapping? Are these cases covered with the current control primitives?
