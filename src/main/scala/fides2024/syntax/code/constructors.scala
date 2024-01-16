package fides2024.syntax.code

// todo add way to define custom Expr, Xctr, ...? As some light syntactic sugar?
//  Make the right abstraction accessible?

final case class CustomExpr[T <: ValType](holes: Tuple, expr: Code[Expr[T]]) extends Expr[T]
// todo Beluga

