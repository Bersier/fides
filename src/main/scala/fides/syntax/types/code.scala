package fides.syntax.types

import fides.syntax.meta.Quoted

import scala.compiletime.ops.int.+
import scala.language.experimental.pureFunctions

/**
  * Parent type of all the Scala types that represent the different types of possible Fides code.
  */
trait CodeType private[syntax]()

/**
  * Fides code type for processes.
  */
type Process = Polar[OffTop, OffBot]

/**
  * [[Polar]] is a generalization of expressions and patterns.
  */
trait Polar[+P >: ValBot, -N <: ValTop] extends CodeType
// TODO seal Polar and always extend Code directly (?)

/**
  * Fides code type for Fides value literals
  */
trait Lit extends CodeType
// TODO Rename to Const, and set Lit[T] = Const & Ntrl[T]?

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
  * Fides code type for extractors (aka patterns). While extractors are really just a special type of
  * process with a single input, they behave differently from a syntactic point of view, as [where their only input
  * comes from] is not represented explicitly by a name, but implicitly by where they are written, dually to
  * expressions. They can be thought of as expressions that are being evaluated backwards, with the syntax for input and
  * output being flipped.
  *
  * Dual of Expr
  */
type Xctr[-T <: ValTop] = Polar[OffTop, T]

type Ntrl[T <: ValTop] = Polar[T, T]

//trait InpLit[+T <: ValTop] extends Expr[T], Lit
//trait OutLit[-T <: ValTop] extends Xctr[T], Lit
//
//trait Lit[T <: ValTop] extends InpLit[T], OutLit[T]
