package fides2024.syntax

final case class Send(message: Expr, recipient: Expr) extends Component
final case class Message(message: Val, recipient: Address) extends Component
