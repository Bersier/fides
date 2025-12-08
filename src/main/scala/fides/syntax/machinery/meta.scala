package fides.syntax.machinery

import typelevelnumbers.binary.Bits

/**
  * Parent type of all the Scala types that represent
  * the different types of possible Fides code,
  * including the full metaprogramming landscape (aka <i>scapes</i>)
  */
trait ConsM[G <: TopG, +Q <: TopQ] private[syntax]() // todo seal
// todo is G really needed? I think so? Covariant or invariant?
type TopM = ConsM[TopG, TopQ]

/**
  * // todo so this doesn't get out of date, make a private documentation class inside QuoteM's companion object
  * `TM <: ConsM[G, ConsQ[P, BotQ]], +M <: ConsM[G, ConsQ[P, TopQ]]`
  *
  * @tparam TM is actually supposed to be derived from M
  *            via the relation [[TrimmedR]]`[`...`, `[[M]]`, `[[TM]]`]` (See [[Quote]])
  */
final abstract class QuoteM[
  P <: TopP, TM <: ConsM[TopG, TopQ], Q <: TopQ,
  M <: ConsM[TopG, ConsQ[P, Q]], // todo should M be covariant or invariant?
] extends ConsM[QuoteG[P, TM, M], Q]

sealed trait EscapeM[G <: TopG, +Q <: TopQ] extends ConsM[G, Q]
object EscapeM:
  final abstract class Head[
    TG <: TopG,
    // todo get G from QuoteD (which would then have an additional invariant G parameter)?
    TM <: ConsM[TG, TopQ], P <: TopP,
    G <: PolarG[QuoteD[TM], P], Q <: TopQ,
    +M <: ConsM[G, Q],
  ] extends EscapeM[TG, ConsQ[P | BotVP, Q]]

  final abstract class Step[
    G <: TopG, Q <: TopQ,
    +M <: EscapeM[G, Q],
  ] extends EscapeM[G, ConsQ[BotP, Q]]
end EscapeM

final abstract class RepeatedM[
  G <: AplrG, Q <: TopQ,
  +M <: ConsM[G, Q],
] extends ConsM[RepeatedG[G], Q]

final abstract class ConcurrentM[
  G <: ArgsUG[AplrG], Q <: TopQ,
  +M <: ConsM[G, Q],
] extends ConsM[ConcurrentG[G], Q]

final abstract class ConjoinM[
  G <: ExprG[CollectedUD[BoolD]], Q <: TopQ,
  +M <: ConsM[G, Q],
] extends ConsM[ConjoinG[G], Q]

final abstract class DisjoinM[
  G <: ExprG[CollectedUD[BoolD]], Q <: TopQ,
  +M <: ConsM[G, Q],
] extends ConsM[DisjoinG[G], Q]

final abstract class NegateM[
  D <: BoolD, P <: TopP,
  G <: PolarG[D, P], Q <: TopQ,
  +M <: ConsM[G, Q],
] extends ConsM[NegateG[D, P, G], Q]

final abstract class EqualM[
  G <: ExprG[CollectedUD[AtomD]], Q <: TopQ,
  +M <: ConsM[G, Q],
] extends ConsM[EqualG[G], Q]

final abstract class RandomBitM[
  Q <: TopQ,
  +M <: ConsM[RandomBitG, Q],
] extends ConsM[RandomBitG, Q]

final abstract class CollectedM[
  D <: TopD, P <: TopP,
  E <: TopE, EG <: PolarG[D, P],
  G <: ArgsG[E, EG], Q <: TopQ,
  +M <: ConsM[G, Q],
] extends ConsM[CollectedG[D, P, E, EG, G], Q]

final abstract class AddElementM[
  D <: TopD, EP <: TopP, P <: TopP,
  EG <: PolarG[D, EP], G <: PolarG[CollectedUD[D], P], EQ <: TopQ, Q <: TopQ,
  +EM <: ConsM[EG, EQ], +M <: ConsM[G, Q],
] extends ConsM[AddElementG[D, EP, P, EG, G], EQ | Q]

final abstract class CollectM[
  K <: TopK, D <: TopD, P >: BotVP <: TopP, B <: Bits,
  SG <: ChannelG[K, D, P], NG <: NtrlG[NatD[B]], SQ <: TopQ, NQ <: TopQ,
  +SM <: ConsM[SG, SQ], +NM <: ConsM[NG, NQ],
] extends ConsM[CollectG[K, D, P, B, SG, NG], SQ | NQ]

final abstract class AddM[
  G <: ExprG[CollectedUD[NatUD]], Q <: TopQ,
  +M <: ConsM[G, Q],
] extends ConsM[AddG[G], Q]

final abstract class MultiplyM[
  G <: ExprG[CollectedUD[NatUD]], Q <: TopQ,
  +M <: ConsM[G, Q],
] extends ConsM[MultiplyG[G], Q]

final abstract class CompareM[
  G1 <: ExprG[NatUD], G2 <: ExprG[NatUD], Q1 <: TopQ, Q2 <: TopQ,
  +M1 <: ConsM[G1, Q1], +M2 <: ConsM[G2, Q2],
] extends ConsM[CompareG[G1, G2], Q1 | Q2]

final abstract class PairM[
  D1 <: TopD, D2 <: TopD, P1 <: TopP, P2 <: TopP,
  G1 <: PolarG[D1, P1], G2 <: PolarG[D2, P2], Q1 <: TopQ, Q2 <: TopQ,
  +M1 <: ConsM[G1, Q1], +M2 <: ConsM[G2, Q2],
] extends ConsM[PairG[D1, D2, P1, P2, G1, G2], Q1 | Q2]
