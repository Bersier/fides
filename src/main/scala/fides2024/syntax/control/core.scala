package fides2024.syntax.control

import fides2024.syntax.identifiers.Cell
import fides2024.syntax.kinds.{Code, Component, Xctr}
import fides2024.syntax.values.{Bool, Pulse}

/**
  * @param running indicates whether the body is currently running
  */
final case class Process(running: Code[Cell[Bool]], body: Code[Component]) extends Component

/**
  * Upon reception of a pulse, the body's execution is started.
  */
final case class OnHold(body: Code[Component]) extends Xctr[Pulse]

/**
  * Upon reception of a pulse, the body's execution is stopped.
  */
final case class Mortal(body: Code[Component]) extends Xctr[Pulse]
// todo it seems wrong that the extractor would be running before being triggered.

/**
  * No message can reach and/or exit the sandboxed component directly. The monitor serves as the intermediate.
  * It can send and receive messages to and from the sandboxed component.
  */
final case class Sandboxed(monitor: Code[Component], sandboxed: Code[Component]) extends Component

final case class Catchable() extends Component
