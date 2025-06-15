package fides.syntax.types

import scala.language.experimental.pureFunctions

/**
  * Parent type of all the Scala types that represent
  * the different types (aka syntactic categories) of possible Fides code.
  */
sealed trait CodeType private[types]()

sealed trait ArgsS[+IsNonEmpty <: Boolean, +S <: CodeType] extends CodeType
type Args[+S <: CodeType] = ArgsS[Boolean, S]

sealed trait CaseS[T <: ValTop, A <: AtomT] extends CodeType

sealed trait TypeS[T <: ValTop] extends CodeType

sealed trait DeclarationS[T <: ValTop] extends CodeType

sealed trait NameS[+T <: ValTop] extends CodeType

sealed trait MNameS[T <: ValTop] extends NameS[T]

/**
  * Fides code type for processes.
  */
sealed trait Process extends CodeType
// TODO rename to NonPo?

/**
  * [[Polar]] is a generalization of expressions and patterns.
  */
sealed trait Polar[+P >: ValBot, -N <: ValTop] extends CodeType

type PoTop = Polar[OffTop, OffBot]

/**
  * Used to mark the code types that represent constants.
  */
private[types] sealed trait Lit extends CodeType

/**
  * Fides code type for expressions. While expressions are really just a special type of process with a single output,
  * they behave differently from a syntactic point of view, as [where their only output goes] is not represented
  * explicitly by a name, but implicitly by where they are written, as is usual with expressions in other languages.
  * This syntactic behavior can be viewed as some kind of mandatory syntactic sugar.
  *
  * Dual of Xctr
  */
type Expr[+T <: ValTop] = Polar[T, OffBot]

/**
  * Fides code type for constants
  *
  * It differs from [[Ntrl]] in that it allows for covariance, which is what we want when a constant is needed.
  */
type Cnst[+T <: ValTop] = Expr[T] & Lit

/**
  * Fides code type for extractors (aka patterns). While extractors are really just a special type of
  * process with a single input, they behave differently from a syntactic point of view, as [where their only input
  * comes from] is not represented explicitly by a name, but implicitly by where they are written, dually to
  * expressions. They can be thought of as expressions that are being evaluated backwards, with the syntax for input and
  * output being flipped.
  *
  * Dual of Expr
  */
type Xctr[-T <: ValTop] = Polar[OffTop, T]

/**
  * Fides code type for Literals
  *
  * Can be used as either an [[Expr]] or as an [[Xctr]]. Is naturally a [[Cnst]].
  */
type Ntrl[T <: ValTop] = Polar[T, T] & Lit

sealed trait BiPo[I <: PoTop, O <: PoTop] extends CodeType
