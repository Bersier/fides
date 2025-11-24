package fides.syntax.types

import scala.language.experimental.pureFunctions

/**
  * Parent type of all the Scala types that represent
  * the different types (aka syntactic categories) of possible Fides code
  */
sealed trait TopS private[types]()

/**
  * A type smaller than the intersection of all code types.
  *
  * Indicates an unreachable code type (in covariant position).
  */
type OffBotS = Nothing

sealed trait ArgsS[+IsNonEmpty <: Boolean, +S <: TopS] extends TopS
type ArgsUS[+S <: TopS] = ArgsS[Boolean, S]

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
sealed trait Polar[+P >: BotT, -N <: TopT, +IsLiteral <: Boolean] extends TopS
type Polr[+P >: BotT, -N <: TopT] = Polar[P, N, Boolean]

type TopPoS = Polr[OffTopT, OffBotT]

/**
  * Fides code type for expressions. While expressions are really just a special type of process with a single output,
  * they behave differently from a syntactic point of view, as [where their only output goes] is not represented
  * explicitly by a name, but implicitly by where they are written, as is usual with expressions in other languages.
  * This syntactic behavior can be viewed as some kind of mandatory syntactic sugar.
  *
  * Dual of Xctr
  *
  * Expressions should probably not be allowed to have any output effects
  * (i.e. no outgoing connections with external code).
  */
type Expr[+T <: TopT] = Polr[T, OffBotT]

/**
  * Fides code type for extractors (aka patterns). While extractors are really just a special type of
  * process with a single input, they behave differently from a syntactic point of view, as [where their only input
  * comes from] is not represented explicitly by a name, but implicitly by where they are written, dually to
  * expressions. They can be thought of as expressions that are being evaluated backwards, with the syntax for input and
  * output being flipped.
  *
  * Dual of Expr
  *
  * Extractors should probably not be allowed to have any input effects
  * (i.e. no incoming connections with external code).
  */
type Xctr[-T <: TopT] = Polr[OffTopT, T]

/**
  * Fides code type for Literals
  *
  * Can be used as either an [[Expr]] or as an [[Xctr]]. Is naturally a [[Cnst]].
  */
type Ntrl[T <: TopT] = Polar[T, T, true]

/**
  * Fides code type for bi-polar process code
  *
  * Bi-polar process code should probably not be allowed to have any side effects
  * (i.e. no connections with external code).
  */
sealed trait Bipo[I <: TopPoS, O <: TopPoS] extends TopS

/**
  * [[Polr]] that is not a literal
  */
type Povr[+P >: BotT, -N <: TopT] = Polar[P, N, false]

/**
  * [[Expr]] that is not a literal
  */
type Exvr[+T <: TopT] = Polar[T, OffBotT, false]

/**
  * Fides code type for constants
  *
  * It differs from [[Ntrl]] in that it allows for covariance, which is what we want when a constant is needed.
  */
type Cnst[+T <: TopT] = Polar[T, OffBotT, true]

/**
  * [[Xctr]] that is not a literal
  */
type Xcvr[-T <: TopT] = Polar[OffTopT, T, false]
