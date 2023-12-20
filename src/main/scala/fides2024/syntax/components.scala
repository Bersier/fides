package fides2024.syntax

final case class Send(message: Expr[Positive], recipient: Expr[Positive]) extends Component
final case class Message(message: Val, recipient: Address) extends Component
