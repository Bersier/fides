package fides2024.syntax

final case class Send(message: Expr[Positive, ValTop], recipient: Expr[Positive, Identifier]) extends Component
final case class Message[T <: ValTop](message: Val[T], recipient: Identifier) extends Component
