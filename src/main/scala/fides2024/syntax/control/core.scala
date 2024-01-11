package fides2024.syntax.control

import fides2024.syntax.identifiers.Inp
import fides2024.syntax.kinds.{Code, Component, Xctr}
import fides2024.syntax.values.Pulse

final case class Asleep(wake: Code[Inp[Pulse]], sleep: Code[Inp[Pulse]], body: Code[Component]) extends Component
final case class Awake(wake: Code[Inp[Pulse]], sleep: Code[Inp[Pulse]], body: Code[Component]) extends Component

final case class OnHold(body: Code[Component]) extends Xctr[Pulse]

final case class Mortal(killSwitch: Code[Inp[Pulse]], body: Code[Component]) extends Component
// todo, instead of separate channels, we could have separate messages for the types of orders (Kill, Stop, Start)

final case class Sandboxed() extends Component

final case class Catchable() extends Component
