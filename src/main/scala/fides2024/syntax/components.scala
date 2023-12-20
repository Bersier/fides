package fides2024.syntax

final case class Send(message: Expr[Positive, ValSup], recipient: Expr[Positive, Identifier]) extends Component
final case class Message[T <: ValSup](message: Val[T], recipient: Val[Identifier]) extends Component
