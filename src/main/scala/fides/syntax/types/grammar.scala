package fides.syntax.types

import scala.language.experimental.pureFunctions

/**
  * Parent type of all the Scala types that represent
  * the different types (aka syntactic categories) of possible Fides code, excluding metaprogramming
  */
sealed trait TopG private[types]()

/**
  * A type smaller than the intersection of all code types.
  *
  * Indicates an unreachable code type (in covariant position).
  */
type OffBotG = Nothing

/**
  * Fides code type for multisets of syntactic elements
  *
  * @tparam E keeps track of whether the collection is empty
  */
final abstract class ArgsG[+E <: TopE, +G <: TopG] extends TopG
type ArgsUG[+G <: TopG] = ArgsG[TopE, G]

final abstract class CaseG[D <: TopD, A <: AtomD] extends TopG

final abstract class TypeG[D <: TopD] extends TopG

final abstract class DeclG[D <: TopD] extends TopG

sealed trait NameG[+D <: TopD] extends TopG

final abstract class MNameG[D <: TopD] extends NameG[D]

/**
  * Fides code type for non-polar process code
  */
final abstract class AplrG extends TopG

sealed trait Polar2G[D <: TopD, +P <: TopP] extends TopG
// todo use

/**
  * [[PolrG]] is a generalization of expressions and patterns.
  */
sealed trait PolarG[+P >: BotD, -N <: TopD, +IsLiteral <: Boolean] extends TopG
type PolrG[+P >: BotD, -N <: TopD] = PolarG[P, N, Boolean]

type TopPoG = PolrG[OffTopD, OffBotD]

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
type ExprG[+D <: TopD] = PolrG[D, OffBotD]
type Expr2G[D <: TopD] = Polar2G[D, GenP[BotB, TopB, TopB]]

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
type XctrG[-D <: TopD] = PolrG[OffTopD, D]
type Xctr2G[D <: TopD] = Polar2G[D, GenP[TopB, BotB, TopB]]

/**
  * Fides code type for Literals
  *
  * Can be used as either an [[ExprG]] or as an [[XctrG]]. Is naturally a [[CnstG]].
  */
type NtrlG[D <: TopD] = PolarG[D, D, true]
type Ntrl2G[D <: TopD] = Polar2G[D, BotP]

/**
  * Fides code type for bi-polar process code
  *
  * Bi-polar process code should probably not be allowed to have any side effects
  * (i.e. no connections with external code).
  */
final abstract class BipoG[I <: TopPoG, O <: TopPoG] extends TopG
// todo make more stuff Bipo? Stuff in generic.scala?

/**
  * [[PolrG]] that is not a literal
  */
type PovrG[+P >: BotD, -N <: TopD] = PolarG[P, N, false]

/**
  * [[ExprG]] that is not a literal
  */
type ExvrG[+D <: TopD] = PolarG[D, OffBotD, false]

/**
  * Fides code type for constants
  *
  * It differs from [[NtrlG]] in that it allows for covariance, which is what we want when a constant is needed.
  */
type CnstG[+D <: TopD] = PolarG[D, OffBotD, true]
type Cnst2G[D <: TopD] = Polar2G[D, GenP[BotB, TopB, BotB]]

/**
  * [[XctrG]] that is not a literal
  */
type XcvrG[-D <: TopD] = PolarG[OffTopD, D, false]

final abstract class ConjoinS[+G <: Expr2G[CollectedUD[BoolD]]] extends Expr2G[BoolD]
final abstract class DisjoinS[+G <: Expr2G[CollectedUD[BoolD]]] extends Expr2G[BoolD]

final abstract class NegateS[
  D <: BoolD, P <: TopP,
  +G <: Polar2G[D, P],
] extends Polar2G[BoolD.Not[D], P]

final abstract class EqualS[+G <: Expr2G[CollectedUD[AtomD]]] extends Expr2G[BoolD]
final abstract class RandomBitS extends Expr2G[BoolD]

final abstract class AddG[+G <: Expr2G[CollectedUD[NatUD]]] extends Expr2G[NatUD]
final abstract class MultiplyG[+G <: Expr2G[CollectedUD[NatUD]]] extends Expr2G[NatUD]
final abstract class CompareG[+G1 <: Expr2G[NatUD], +G2 <: Expr2G[NatUD]] extends Expr2G[NatUD]

final abstract class PairG[
  D1 <: TopD, D2 <: TopD, +P <: TopP,
  +G1 <: Polar2G[D1, P], +G2 <: Polar2G[D2, P],
] extends Polar2G[PairD[D1, D2], P]

final abstract class QuoteG[
  G <: TopG, P <: TopP,
  +RM <: ConsM[G, ConsQ[P, BotQ]],
] extends Polar2G[QuoteD[G], P]

// todo summon[QuoteD[QuoteG[QuoteG[?, ?, RM], ?, ?]]] is invariant in RM!
//  This might be the key to matching escape matchers at nauseam.

final abstract class WrapG[
  D <: TopD,
  +G <: Expr2G[D],
] extends Expr2G[QuoteD[Ntrl2G[D]]]
