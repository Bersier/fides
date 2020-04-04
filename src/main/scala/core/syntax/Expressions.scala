package core.syntax

sealed trait Expr[T <: Val] extends InLoc[T]

final case class Constant[T <: Val](value: T) extends Expr[T]

final case class Join[S <: Val, T <: Val](one: InLoc[S], two: InLoc[T]) extends Expr[APair[S, T]]
