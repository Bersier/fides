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

/**
  * Fides code type for multisets of syntactic elements
  */
final abstract class ArgsS[+IsNonEmpty <: Bool, +S <: TopS] extends TopS
type ArgsUS[+S <: TopS] = ArgsS[Bool, S]

final abstract class CaseS[T <: TopT, A <: AtomT] extends TopS

final abstract class TypeS[T <: TopT] extends TopS

final abstract class DeclS[T <: TopT] extends TopS

sealed trait NameS[+T <: TopT] extends TopS

final abstract class MNameS[T <: TopT] extends NameS[T]

/**
  * Fides code type for non-polar process code
  */
final abstract class Aplr extends TopS

sealed trait Polar2[T <: TopT, +P <: TopP] extends TopS
// todo use

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
type Expr2[T <: TopT] = Polar2[T, Polarity[Bool.T, Bool, Bool]]

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
type Xctr2[T <: TopT] = Polar2[T, Polarity[Bool, Bool.T, Bool]]

/**
  * Fides code type for Literals
  *
  * Can be used as either an [[Expr]] or as an [[Xctr]]. Is naturally a [[Cnst]].
  */
type Ntrl[T <: TopT] = Polar[T, T, true]
type Ntrl2[T <: TopT] = Polar2[T, BotP]

/**
  * Fides code type for bi-polar process code
  *
  * Bi-polar process code should probably not be allowed to have any side effects
  * (i.e. no connections with external code).
  */
final abstract class Bipo[I <: TopPoS, O <: TopPoS] extends TopS
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

final abstract class PairS[
  T1 <: TopT,
  T2 <: TopT,
  +P1 <: TopP,
  +P2 <: TopP,
] extends Polar2[PairT[T1, T2], P1 | P2]
// todo more of this
