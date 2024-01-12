package fides2024.syntax.conditionals

import fides2024.syntax.code.{Atom, Code, CodeType, Component, Expr, Ptrn, Val, ValType, VarArgs, Xctr}

final case class SwitchComponent[T <: ValType, A <: Atom](
  input    : Code[Expr[T]],
  condition: Code[Expr[A]],
  cases    : Code[VarArgs[XctrCase[T, A]]],
  default  : Code[Xctr[T]],
) extends Component

final case class SwitchExpr[T <: ValType, A <: Atom]
(condition: Code[Expr[A]], cases: Code[VarArgs[ExprCase[T, A]]], default: Code[Expr[T]]) extends Expr[T]
// todo eager?

final case class Switch[A <: Atom](cases : Code[VarArgs[Case[A]]]) extends Ptrn[A, A]
// todo violates polarity reversal interdiction

final case class Case[A <: Atom](testValue: Code[Val[A]], body: Code[Component]) extends Code[Case[A]], CodeType

final case class XctrCase[T <: ValType, A <: Atom]
(testValue: Code[Val[A]], exctractor: Xctr[T]) extends Code[XctrCase[T, A]], CodeType

final case class ExprCase[T <: ValType, A <: Atom]
(testValue: Code[Val[A]], value: Expr[T]) extends Code[ExprCase[T, A]], CodeType

// todo Scala code also has nested polarity reversals, at least within Exprs.
