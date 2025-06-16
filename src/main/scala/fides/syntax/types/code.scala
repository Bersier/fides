package fides.syntax.types

import scala.language.experimental.pureFunctions

/**
  * Parent type of all the Scala types that represent
  * the different types (aka syntactic categories) of possible Fides code
  */
sealed trait TopS private[types]()

sealed trait ArgsS[+IsNonEmpty <: Boolean, +S <: TopS] extends TopS
type Args[+S <: TopS] = ArgsS[Boolean, S]

sealed trait CaseS[T <: TopT, A <: AtomT] extends TopS

sealed trait TypeS[T <: TopT] extends TopS

sealed trait DeclS[T <: TopT] extends TopS

sealed trait NameS[+T <: TopT] extends TopS

sealed trait MNameS[T <: TopT] extends NameS[T]

/**
  * Fides code type for non-polar process code
  */
sealed trait Aplr extends TopS

/**
  * [[Polr]] is a generalization of expressions and patterns.
  */
sealed trait PolrS[+P >: BotT, -N <: TopT, +IsLiteral <: Boolean] extends TopS
type Polr[+P >: BotT, -N <: TopT] = PolrS[P, N, Boolean]

type TopPoS = PolrS[OffTopT, OffBotT, Boolean]

/**
  * Fides code type for expressions. While expressions are really just a special type of process with a single output,
  * they behave differently from a syntactic point of view, as [where their only output goes] is not represented
  * explicitly by a name, but implicitly by where they are written, as is usual with expressions in other languages.
  * This syntactic behavior can be viewed as some kind of mandatory syntactic sugar.
  *
  * Dual of Xctr
  */
type Expr[+T <: TopT] = PolrS[T, OffBotT, Boolean]

/**
  * Fides code type for extractors (aka patterns). While extractors are really just a special type of
  * process with a single input, they behave differently from a syntactic point of view, as [where their only input
  * comes from] is not represented explicitly by a name, but implicitly by where they are written, dually to
  * expressions. They can be thought of as expressions that are being evaluated backwards, with the syntax for input and
  * output being flipped.
  *
  * Dual of Expr
  */
type Xctr[-T <: TopT] = PolrS[OffTopT, T, Boolean]

/**
  * Fides code type for Literals
  *
  * Can be used as either an [[Expr]] or as an [[Xctr]]. Is naturally a [[Cnst]].
  */
type Ntrl[T <: TopT] = PolrS[T, T, true]

/**
  * Fides code type for bi-polar process code
  */
sealed trait Bipo[I <: TopPoS, O <: TopPoS] extends TopS

/**
  * [[Polr]] that is not a literal
  */
type Povr[+P >: BotT, -N <: TopT] = PolrS[P, N, false]

/**
  * [[Expr]] that is not a literal
  */
type Exvr[+T <: TopT] = PolrS[T, OffBotT, false]

/**
  * Fides code type for constants
  *
  * It differs from [[Ntrl]] in that it allows for covariance, which is what we want when a constant is needed.
  */
type Cnst[+T <: TopT] = PolrS[T, OffBotT, true]

/**
  * [[Xctr]] that is not a literal
  */
type Xcvr[-T <: TopT] = PolrS[OffTopT, T, Boolean]
