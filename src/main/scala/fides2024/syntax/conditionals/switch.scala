package fides2024.syntax.conditionals

import fides2024.syntax.identifiers.Identifier
import fides2024.syntax.kinds.{Code, CodeType, Component, Expr, Ptrn, Val, ValType, VarArgs, Xctr}

final case class SwitchComponent[T <: ValType](
  input    : Code[Expr[T]],
  condition: Code[Expr[Identifier]],
  cases    : Code[VarArgs[XctrCase[T]]],
) extends Component

final case class Switch(cases : Code[VarArgs[Case]]) extends Ptrn[Identifier, Identifier]

final case class Case(testValue: Code[Val[Identifier]], body: Code[Component]) extends Code[Case], CodeType

final case class XctrCase[T <: ValType]
(testValue: Code[Val[Identifier]], body: Xctr[T]) extends Code[XctrCase[T]], CodeType
