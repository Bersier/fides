package fides.syntax.types

import scala.compiletime.ops.boolean.&&
import scala.language.experimental.pureFunctions

/**
  * Parent type of all the Scala types that represent
  * the different types (aka syntactic categories) of possible Fides code
  */
sealed trait TopS private[types]()

/**
  * Lower bound for - or intersection of - all Fides code types
  */
type BotS = Nothing

type OffTopS = Any

/**
  * A type smaller than [[BotT]].
  *
  * Indicates an unreachable code type (in covariant position).
  */
type OffBotS = Nothing

/**
  * Fides code type for multisets of syntactic elements
  */
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

sealed trait Polar2[T <: TopT, P <: Boolean, N <: Boolean, C <: Boolean] extends TopS

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
  *
  * Fides is strongly typed in the sense that no expression can get stuck due to a type mismatch.
  * If an expression of a given data type evaluates, it always evaluates to a value of that data type.
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
  *
  * Fides is strongly typed in the sense that no extractor can get stuck due to a type mismatch.
  * If an extractor of a given data type is given a value of that type,
  * it never chokes on it (like a refutable pattern could).
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
// todo make more stuff Bipo? Stuff in generic.scala?

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

/**
  * [[S1]] and [[S2]] are the main type parameters. The others are auxiliary, used for threading.
  */
sealed trait PairS[
  P1 >: BotT,
  P2 >: BotT,
  N1 <: TopT,
  N2 <: TopT,
  L1 <: Boolean,
  L2 <: Boolean,
  +S1 <: Polar[P1, N1, L1],
  +S2 <: Polar[P2, N2, L2],
] extends Polar[PairT[P1, P2], PairT[N1, N2], L1 && L2]
