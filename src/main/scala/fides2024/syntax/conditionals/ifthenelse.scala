package fides2024.syntax.conditionals

import fides2024.syntax.kinds.{Code, Component, Expr, ValType, Xctr}
import fides2024.syntax.values.Bool

final case class IfThenElseComponent[T <: ValType](
  input      : Code[Expr[T]],
  condition  : Code[Expr[Bool]],
  trueBranch : Code[Xctr[T]],
  falseBranch: Code[Xctr[T]],
) extends Component

final case class IfThenElseComponentShort(
  condition  : Code[Expr[Bool]],
  trueBranch : Code[Component],
  falseBranch: Code[Component],
) extends Component

final case class IfThenElseExpr[T <: ValType]
(condition: Code[Expr[Bool]], trueBranch: Code[Expr[T]], falseBranch: Code[Expr[T]]) extends Expr[T]
