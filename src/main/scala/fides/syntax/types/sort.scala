package fides.syntax.types

import util.{BoolT, BotB, TopB}

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
  *
  * @tparam I keeps track of whether the collection is populated (i.e. non-empty)
  */
final abstract class ArgsS[+I <: BoolT, +S <: TopS] extends TopS
type ArgsUS[+S <: TopS] = ArgsS[BoolT, S]

final abstract class CaseS[D <: TopD, A <: AtomD] extends TopS

final abstract class TypeS[D <: TopD] extends TopS

final abstract class DeclS[D <: TopD] extends TopS

sealed trait NameS[+D <: TopD] extends TopS

final abstract class MNameS[D <: TopD] extends NameS[D]

/**
  * Fides code type for non-polar process code
  */
final abstract class AplrS extends TopS

sealed trait Polar2S[D <: TopD, +P <: TopP] extends TopS
// todo use

/**
  * [[PolrS]] is a generalization of expressions and patterns.
  */
sealed trait PolarS[+P >: BotD, -N <: TopD, +IsLiteral <: Boolean] extends TopS
type PolrS[+P >: BotD, -N <: TopD] = PolarS[P, N, Boolean]

type TopPoS = PolrS[OffTopD, OffBotD]

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
type ExprS[+D <: TopD] = PolrS[D, OffBotD]
type Expr2S[D <: TopD] = Polar2S[D, Polarity[BotB, TopB, TopB]]

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
type XctrS[-D <: TopD] = PolrS[OffTopD, D]
type Xctr2S[D <: TopD] = Polar2S[D, Polarity[TopB, BotB, TopB]]

/**
  * Fides code type for Literals
  *
  * Can be used as either an [[ExprS]] or as an [[XctrS]]. Is naturally a [[CnstS]].
  */
type NtrlS[D <: TopD] = PolarS[D, D, true]
type Ntrl2S[D <: TopD] = Polar2S[D, BotP]

/**
  * Fides code type for bi-polar process code
  *
  * Bi-polar process code should probably not be allowed to have any side effects
  * (i.e. no connections with external code).
  */
final abstract class BipoS[I <: TopPoS, O <: TopPoS] extends TopS
// todo make more stuff Bipo? Stuff in generic.scala?

/**
  * [[PolrS]] that is not a literal
  */
type PovrS[+P >: BotD, -N <: TopD] = PolarS[P, N, false]

/**
  * [[ExprS]] that is not a literal
  */
type ExvrS[+D <: TopD] = PolarS[D, OffBotD, false]

/**
  * Fides code type for constants
  *
  * It differs from [[NtrlS]] in that it allows for covariance, which is what we want when a constant is needed.
  */
type CnstS[+D <: TopD] = PolarS[D, OffBotD, true]
type Cnst2S[D <: TopD] = Polar2S[D, Polarity[BotB, TopB, BotB]]

/**
  * [[XctrS]] that is not a literal
  */
type XcvrS[-D <: TopD] = PolarS[OffTopD, D, false]

final abstract class PairS[
  D1 <: TopD, D2 <: TopD, +P <: TopP,
  +S1 <: Polar2S[D1, P], +S2 <: Polar2S[D2, P],
] extends Polar2S[PairD[D1, D2], P]

final abstract class QuoteS[
  S <: TopS, P <: TopP,
  +RM <: ConsM[S, ConsQ[P, BotQ]],
] extends Polar2S[QuoteD[S], P]

// todo summon[QuoteD[QuoteS[QuoteS[?, ?, RM], ?, ?]]] is invariant in RM!
//  This might be the key to matching escape matchers at nauseam.

final abstract class WrapS[
  D <: TopD,
  +S <: Expr2S[D],
] extends Expr2S[QuoteD[Ntrl2S[D]]]
