package fides2024.syntax

trait Code[T <: CodeType] private[syntax]()
trait CodeType private[syntax]()
trait ValType private[syntax]()

/**
  * Component also extends Code[Component] purely for convenience, so that we can write
  * "Foo extends Component", rather than "Foo extends Component, Code[Component]".
  */
trait Component extends CodeType, Code[Component]

type Polar = [_ <: ValType] =>> CodeType
trait Expr[+T <: ValType] extends CodeType
trait Ptrn[-T <: ValType] extends CodeType

/**
  * Val[T] also extends Code[Val[T]] and ValType purely for convenience, so that we can write
  * "Foo extends Val[Foo]", rather than "Foo extends Val[Foo], Code[Val[Foo]], ValType".
  */
trait Val[T <: ValType] extends Expr[T], Ptrn[T], Code[Val[T]], ValType

//type PolarFromB[B <: Boolean] <: Polar = B match
//  case true => Expr
//  case false => Ptrn
