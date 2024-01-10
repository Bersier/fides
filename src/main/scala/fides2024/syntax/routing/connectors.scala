package fides2024.syntax.routing

import fides2024.syntax.identifiers.Inp
import fides2024.syntax.kinds.{Code, Component, Expr, ValType, Xctr}
import fides2024.syntax.values.{Bool, U}

/**
  * A hard-coded connection between one input and one output
  */
final case class Forward[T <: ValType](input: Code[Expr[T]], output: Code[Xctr[T]]) extends Component

/**
  * Kind-of the dual of values.
  *
  * Aka Sink, Forget
  */
final case class Ignore() extends Xctr[ValType]

final case class Duplicate[T <: ValType]
(value: Code[Expr[T]], first: Code[Xctr[T]], second: Code[Xctr[T]]) extends Component

final case class IfThenElseComponent[T <: ValType](
  input: Code[Expr[T]],
  condition: Code[Expr[Bool]],
  trueBranch: Code[Xctr[T]],
  falseBranch: Code[Xctr[T]],
) extends Component

final case class IfThenElseExpr[T <: ValType]
(condition: Code[Expr[Bool]], trueBranch: Code[Expr[T]], falseBranch: Code[Expr[T]]) extends Expr[T]

final case class Hold[T <: ValType](signal: Code[Expr[U.type]], value: Code[Expr[T]]) extends Expr[T]

final case class IgnoreV(value: Code[Expr[?]]) extends Expr[U.type]
final case class IgnoreVNeg(waiting: Code[Xctr[U.type]]) extends Xctr[ValType]

final case class Either[T <: ValType](first: Code[Expr[T]], second: Code[Expr[T]]) extends Expr[T]

final case class Asleep(wake: Code[Inp[U.type]], sleep: Code[Inp[U.type]], body: Code[Component]) extends Component
final case class Awake(wake: Code[Inp[U.type]], sleep: Code[Inp[U.type]], body: Code[Component]) extends Component

final case class Wait(body: Code[Component]) extends Xctr[U.type]
