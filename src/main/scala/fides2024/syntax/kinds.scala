package fides2024.syntax

/**
  * General type to represent Fides code
  */
trait Code[+T <: CodeType] private[syntax]()

/**
  * Represents the different types of possible Fides code.
  */
trait CodeType private[syntax]()

/**
  * Parent type of all the types that represent Fides value types.
  */
sealed trait ValType private[syntax]()

/**
  * Component also extends Code[Component] purely for convenience, so that we can write
  * "Foo extends Component", rather than "Foo extends Component, Code[Component]".
  */
trait Component extends CodeType, Code[Component]

/**
  * Higher-kinded type, that, in practice, could be either Expr, Ptrn, or Val.
  */
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

/**
  * Val[T] also extends Code[Val[T]] and ValType purely for convenience, so that we can write
  * "Foo extends Val[Foo]", rather than "Foo extends Val[Foo], Code[Val[Foo]], ValType".
  */
trait Val[T <: ValType] extends Expr[T], Ptrn[T], Code[Val[T]], ValType
