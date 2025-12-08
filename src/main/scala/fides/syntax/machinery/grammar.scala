package fides.syntax.machinery

import typelevelnumbers.binary.Bits

import scala.annotation.unchecked.uncheckedVariance

/**
  * Parent type of all the Scala types that represent
  * the different types (aka syntactic categories) of possible Fides code, excluding metaprogramming
  */
sealed trait TopG private[machinery]()

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
// todo this is losing information, which is problematic
type ArgsUG[+G <: TopG] = ArgsG[TopE, G]

final abstract class CaseG[D <: TopD, A <: AtomD] extends TopG

final abstract class TypeG[D <: TopD] extends TopG

final abstract class DeclG[D <: TopD] extends TopG

@deprecated
sealed trait OldNameG[+D <: TopD] extends TopG

@deprecated
final abstract class MNameG[D <: TopD] extends OldNameG[D]

/**
  * Fides code type for non-polar process code
  */
sealed trait AplrG extends TopG

final abstract class RepeatedG[+G <: AplrG] extends AplrG
final abstract class ConcurrentG[+G <: ArgsUG[AplrG]] extends AplrG

final abstract class NameG[+K <: TopK] extends TopG
type LauncherNameG = NameG[LauncherK.type]

/*
 * todo
 *
 * Channels
 * Immutable variables
 * Read-once variables
 * Linear variables
 * Cells
 * Signatures
 *
 * VariableProperties[+Mutability <: TopB, +Linearity <: TopB, +Synchronicity <: TopB]
 *
 * Name
 * Identifier(name)
 * Channel(name, type) <: InpChannel(name, type)
 * Address(Channel(name, type))
 */
sealed trait VarG[+K <: TopK] extends TopG

sealed trait LocG[+K <: TopK, D <: TopD, +P >: BotVP <: TopP] extends VarG[K]

final abstract class ChannelG[+K <: TopK, D <: TopD, +P >: BotVP <: TopP] extends LocG[K, D, P]

type ChanG[+K <: TopK, D <: TopD] = ChannelG[K, D, BotVP]

type InpChanG[+K <: TopK, +D <: TopD] = ChannelG[K, D @uncheckedVariance, GenP[BotB, TopB, TopB]]

type OutChanG[+K <: TopK, -D <: TopD] = ChannelG[K, D @uncheckedVariance, GenP[TopB, BotB, TopB]]

/**
  * [[PolarG]] is a generalization of expressions and patterns.
  */
sealed trait PolarG[D <: TopD, +P <: TopP] extends TopG
type PosG[+D <: TopD, +C <: TopB] = PolarG[D @uncheckedVariance, GenP[BotB, TopB, C]]
type NegG[-D <: TopD, +C <: TopB] = PolarG[D @uncheckedVariance, GenP[TopB, BotB, C]]

@deprecated
sealed trait OldPolarG[+P >: BotD, -N <: TopD, +IsLiteral <: Boolean] extends TopG
@deprecated
type PolrG[+P >: BotD, -N <: TopD] = OldPolarG[P, N, Boolean]

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
type ExprG[+D <: TopD] = PosG[D, TopB]

@deprecated
type OldExprG[+D <: TopD] = PolrG[D, OffBotD]

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
type XctrG[-D <: TopD] = NegG[D, TopB]

@deprecated
type OldXctrG[-D <: TopD] = PolrG[OffTopD, D]

/**
  * Fides code type for Literals
  *
  * Can be used as either an [[OldExprG]] or as an [[OldXctrG]]. Is naturally a [[OldCnstG]].
  */
type NtrlG[D <: TopD] = PolarG[D, BotP]

@deprecated
type OldNtrlG[D <: TopD] = OldPolarG[D, D, true]

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
@deprecated
type PovrG[+P >: BotD, -N <: TopD] = OldPolarG[P, N, false]

/**
  * [[OldExprG]] that is not a literal
  */
@deprecated
type ExvrG[+D <: TopD] = OldPolarG[D, OffBotD, false]

/**
  * Fides code type for constants
  *
  * It differs from [[OldNtrlG]] in that it allows for covariance, which is what we want when a constant is needed.
  */
type CnstG[+D <: TopD] = PosG[D, BotB]

@deprecated
type OldCnstG[+D <: TopD] = OldPolarG[D, OffBotD, true]

/**
  * [[OldXctrG]] that is not a literal
  */
@deprecated
type XcvrG[-D <: TopD] = OldPolarG[OffTopD, D, false]

final abstract class ConjoinG[+G <: ExprG[CollectedUD[BoolD]]] extends ExprG[BoolD]
final abstract class DisjoinG[+G <: ExprG[CollectedUD[BoolD]]] extends ExprG[BoolD]

final abstract class NegateG[
  D <: BoolD, P <: TopP,
  +G <: PolarG[D, P],
] extends PolarG[BoolD.Not[D], P | BotVP]

final abstract class EqualG[+G <: ExprG[CollectedUD[AtomD]]] extends ExprG[BoolD]
final abstract class RandomBitG extends ExprG[BoolD]

final abstract class CollectedG[
  D <: TopD, P <: TopP,
  E <: TopE, EG <: PolarG[D, P],
  +G <: ArgsG[E, EG],
] extends PolarG[CollectedD[E, D], P]

final abstract class AddElementG[
  D <: TopD, EP <: TopP, P <: TopP,
  +EG <: PolarG[D, EP], +G <: PolarG[CollectedUD[D], P],
] extends PolarG[CollectedD[TopE.F, D], EP | P]

final abstract class CollectG[
  K <: TopK, D <: TopD, P >: BotVP <: TopP, B <: Bits,
  +SG <: ChannelG[K, D, P], +NG <: NtrlG[NatD[B]],
] extends PolarG[CollectedUD[D], P]

final abstract class AddG[+G <: ExprG[CollectedUD[NatUD]]] extends ExprG[NatUD]
final abstract class MultiplyG[+G <: ExprG[CollectedUD[NatUD]]] extends ExprG[NatUD]
final abstract class CompareG[+G1 <: ExprG[NatUD], +G2 <: ExprG[NatUD]] extends ExprG[BoolD]

final abstract class PairG[
  D1 <: TopD, D2 <: TopD, P1 <: TopP, P2 <: TopP,
  +G1 <: PolarG[D1, P1], +G2 <: PolarG[D2, P2],
] extends PolarG[PairD[D1, D2], P1 | P2]

/**
  * `TM <: ConsM[G, ConsQ[P, BotQ]], +M <: ConsM[G, ConsQ[P, TopQ]]`
  *
  * @tparam TM is actually supposed to be derived from M
  *            via the relation [[TrimmedR]]`[`...`, `[[M]]`, `[[TM]]`]` (See [[Quote]])
  */
final abstract class QuoteG[
  P <: TopP, TM <: TopM,
  +M <: ConsM[TopG, ConsQ[P, TopQ]], // todo is that the correct variance?
] extends PolarG[QuoteD[TM], P]

// todo summon[QuoteD[QuoteG[QuoteG[?, ?, RM], ?, ?]]] is invariant in RM!
//  This might be the key to matching escape matchers at nauseam.

final abstract class WrapG[
  D <: TopD,
  +G <: PolarG[D, GenP[BotB, TopB, TopB]],
] extends ExprG[QuoteD[ConsM[NtrlG[D], BotQ]]]
