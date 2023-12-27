package fides2024.syntax

trait Code[+T <: CodeType] private[syntax]()
trait CodeType private[syntax]()
trait ValType private[syntax]()

/**
  * Component also extends Code[Component] purely for convenience, so that we can write
  * "Foo extends Component", rather than "Foo extends Component, Code[Component]".
  */
trait Component extends CodeType, Code[Component]

type Polar = [T <: ValType] =>> CodeType

/**
  * Expr[T] also extends Code[Expr[T]] purely for convenience, so that we can write
  * "Foo extends Expr[Foo]", rather than "Foo extends Expr[Foo], Code[Expr[Foo]]".
  */
trait Expr[+T <: ValType] extends CodeType, Code[Expr[T]]

/**
  * Ptrn[T] also extends Code[Ptrn[T]] purely for convenience, so that we can write
  * "Foo extends Ptrn[Foo]", rather than "Foo extends Ptrn[Foo], Code[Ptrn[Foo]]".
  */
trait Ptrn[-T <: ValType] extends CodeType, Code[Ptrn[T]]

//trait PosVal[+T <: ValType] extends Expr[T]
//trait NegVal[-T <: ValType] extends Ptrn[T]

/**
  * Val[T] also extends Code[Val[T]] and ValType purely for convenience, so that we can write
  * "Foo extends Val[Foo]", rather than "Foo extends Val[Foo], Code[Val[Foo]], ValType".
  */
trait Val[T <: ValType] extends Expr[T], Ptrn[T], Code[Val[T]], ValType

type PolarFromB[B <: Boolean, T <: ValType] <: CodeType = B match
  case true => Expr[T]
  case false => Ptrn[T]
