package fides2024.syntax

final case class Send(message: Expr[Positive, ValSup], recipient: Expr[Positive, Identifier]) extends Component
final case class Message[T <: ValSup](message: Val[T], recipient: Val[Identifier]) extends Component

final case class Scope(localIdentifiers: Val[Collection[Identifier]], body: Component) extends Component

final case class Repeated(body: Component) extends Component

// todo Collection[Component] is not a valid type...
final case class Concurrent(components: Val[Collection[?]]) extends Component
