package fides2024.syntax

final case class Send(message: Code[Expr[ValType]], recipient: Code[Expr[Identifier]]) extends Component

final case class Message[T <: ValType](message: Code[Val[T]], recipient: Code[Val[Identifier]]) extends Component

final case class Scope(localIdentifiers: Code[Val[Collection[Identifier]]], body: Code[Component]) extends Component

final case class Repeated(body: Code[Component]) extends Component

// todo Collection[Component] is not a valid type...
final case class Concurrent(components: Val[Collection[?]]) extends Component
