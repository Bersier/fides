package fides2024.syntax.connectors

import fides2024.syntax.code.{Atom, Code, CodeType, Component, Expr, Ptrn, Val, ValType, VarArgs, Xctr}

/**
  * Tries to match a value to the given pattern. Upon failure, outputs the value to the alternative instead.
  */
final case class Match[T <: ValType](pattern: Code[Ptrn[T, T]], alternative: Code[Xctr[T]]) extends Xctr[T]

final case class Switch[T <: ValType, A <: Atom](
  input  : Code[Expr[T]],
  testee : Code[Expr[A]],
  cases  : Code[VarArgs[Case[T, A]]],
  default: Code[Xctr[T]],
) extends Component

final case class Case[T <: ValType, A <: Atom]
(testValue: Code[Val[A]], exctractor: Code[Xctr[T]]) extends Code[Case[T, A]], CodeType
