package fides2024.syntax

/**
  * General type to represent Fides code
  */
trait Code[+T <: CodeType] private[syntax]()

/**
  * Parent type of all the Scala types that represent the different types of possible Fides code.
  */
trait CodeType private[syntax]()

/**
  * Parent type of all the Scala types that represent Fides value types.
  */
sealed trait ValType private[syntax]()

/**
  * Fides code type for components.
  *
  * For convenience, Component also extends Code[Component], so that we can write
  * "Foo extends Component", rather than "Foo extends Component, Code[Component]".
  */
trait Component extends CodeType, Code[Component]

/**
  * Higher-kinded type alias, used to upper-bound type parameters,
  * that, in practice, could be either Expr, Ptrn, or Val.
  */
type Polar = [T <: ValType] =>> CodeType

/**
  * Fides code type for expressions. While expressions are really just a special type of component with a single output,
  * they behave differently from a syntactic point of view, as [where their only output goes] is not represented
  * explicitly by a name, but implicitly by where they are written, as is usual with expressions in other languages.
  * This syntactic behavior could be viewed as some kind of mandatory syntactic sugar.
  *
  * Dual of Ptrn
  *
  * For convenience, Expr[T] also extends Code[Expr[T]], so that we can write
  * "Foo extends Expr[Foo]", rather than "Foo extends Expr[Foo], Code[Expr[Foo]]".
  */
trait Expr[+T <: ValType] extends CodeType, Code[Expr[T]]

/**
  * Fides code type for patterns. While patterns are really just a special type of component with a single input,
  * they behave differently from a syntactic point of view, as [where their only input comes from] is not represented
  * explicitly by a name, but implicitly by where they are written, dually to expressions. They can be thought of as
  * expressions that are being evaluated backwards, with the syntax for input and output being flipped.
  *
  * Dual of Expr
  *
  * For convenience, Ptrn[T] also extends Code[Ptrn[T]], so that we can write
  * "Foo extends Ptrn[Foo]", rather than "Foo extends Ptrn[Foo], Code[Ptrn[Foo]]".
  *
  * @tparam T ValType lower bound, for the type of non-values in the pattern
  * @tparam U ValType upper bound, for the type of values in the pattern
  */
trait Pattern[-T <: ValType, +U <: T] extends CodeType, Code[Pattern[T, U]]
type Ptrn = [T <: ValType] =>> Pattern[T, T]

/**
  * Fides code type for Fides values.
  *
  * For convenience, Val[T] also extends Code[Val[T]] and ValType, so that we can write
  * "Foo extends Val[Foo]", rather than "Foo extends Val[Foo], Code[Val[Foo]], ValType".
  *
  * @tparam T keeps track of the value type
  */
trait Val[+T <: ValType] extends Expr[T], Pattern[ValType, T], Code[Val[T]], ValType
// todo should we really have covariance/subtyping?