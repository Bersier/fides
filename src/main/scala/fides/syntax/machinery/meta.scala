package fides.syntax.machinery

import typelevelnumbers.binary.Bits

/**
  * Parent type of all the Scala types that represent
  * the different types of possible Fides code,
  * including the full metaprogramming landscape (aka <i>scapes</i>)
  */
trait ConsM[+G <: TopG, +Q <: TopQ] private[syntax]() // todo seal
// todo maybe Q should not be in here at all, but rather in Code
type TopM = ConsM[TopG, TopQ]

/**
  * Helper type that is invariant in [[G]].
  */
private[syntax] sealed trait ConsHM[G <: TopG, +Q <: TopQ] extends ConsM[G, Q]

/**
  * // todo so this doesn't get out of date, make a private documentation class inside QuoteM's companion object
  * `TM <: ConsM[G, ConsQ[P, BotQ]], +M <: ConsM[G, ConsQ[P, TopQ]]`
  *
  * @tparam TM is actually supposed to be derived from M
  *            via the relation [[TrimmedR]]`[`...`, `[[M]]`, `[[TM]]`]` (See [[Quote]])
  */
final abstract class QuoteM[
  K <: TopK, KQ <: TopQ, P <: TopP, Q <: TopQ, TM <: TopM,
  +KM <: ConsM[NameG[K], KQ], M <: ConsM[TopG, ConsQ[P, Q]], // todo should M be covariant or invariant?
] extends ConsHM[QuoteG[P, TM, K, M], Q | KQ]

final abstract class EscapeM[
  TG <: TopG,
  K <: TopK, KQ <: TopQ, TM <: ConsHM[TG, TopQ], P <: TopP, Q <: TopQ,
  // todo P and Q are loose, as I suspect they are in many places.
  //  Is that an issue? We could tighten them with helper types.
  +KM <: ConsM[NameG[K], KQ], +M <: ConsM[PolarG[QuoteD[TM], P], Q],
] extends ConsHM[TG, ConsQ[P | BotVP, Q]]

final abstract class ArgsM[
  E <: TopE, G <: TopG, Q <: TopQ,
  +M <: ConsHM[G, Q],
] extends ConsHM[ArgsG[E, G], Q]

final abstract class ZipM[
  EG <: TopG, EQ <: TopQ,
  E <: TopE, EM <: ConsHM[EG, EQ], P <: TopP,
  G <: PolarG[CollectedD[E, QuoteD[EM]], P], Q <: TopQ,
  +M <: ConsHM[G, Q],
] extends ConsHM[ZipG[EG, EQ, E, EM, P, G], EQ | Q]

final abstract class WrapM[
  D <: TopD,
  G <: ExprHG[D], Q <: TopQ,
  +M <: ConsHM[G, Q],
] extends ConsHM[WrapG[D, G], Q]

final abstract class EvalM[
  D <: TopD, QQ <: TopQ,
  G <: ExprG[QuoteD[ConsM[ExprHG[D], QQ]]], Q <: TopQ,
  +M <: ConsHM[G, Q],
] extends ConsHM[EvalG[D, QQ, G], Q | QQ]
// todo Qs

final abstract class RepeatedM[
  G <: AplrG, Q <: TopQ,
  +M <: ConsHM[G, Q],
] extends ConsHM[RepeatedG[G], Q]

final abstract class ConcurrentM[
  G <: ArgsUG[AplrG], Q <: TopQ,
  +M <: ConsHM[G, Q],
] extends ConsHM[ConcurrentG[G], Q]

final abstract class ConjoinM[
  G <: ExprG[CollectedUD[BoolD]], Q <: TopQ,
  +M <: ConsHM[G, Q],
] extends ConsHM[ConjoinG[G], Q]

final abstract class DisjoinM[
  G <: ExprG[CollectedUD[BoolD]], Q <: TopQ,
  +M <: ConsHM[G, Q],
] extends ConsHM[DisjoinG[G], Q]

final abstract class NegateM[
  D <: BoolD, P <: TopP,
  G <: PolarG[D, P], Q <: TopQ,
  +M <: ConsHM[G, Q],
] extends ConsHM[NegateG[D, P, G], Q]

final abstract class EqualM[
  G <: ExprG[CollectedUD[AtomD]], Q <: TopQ,
  +M <: ConsHM[G, Q],
] extends ConsHM[EqualG[G], Q]

final abstract class RandomBitM[
  Q <: TopQ,
  +M <: ConsM[RandomBitG, Q],
] extends ConsHM[RandomBitG, Q]

final abstract class CollectedM[
  D <: TopD, P <: TopP,
  E <: TopE, EG <: PolarG[D, P],
  G <: ArgsG[E, EG], Q <: TopQ,
  +M <: ConsHM[G, Q],
] extends ConsHM[CollectedG[D, P, E, EG, G], Q]

final abstract class AddElementM[
  D <: TopD, EP <: TopP, P <: TopP,
  EG <: PolarG[D, EP], G <: PolarG[CollectedUD[D], P], EQ <: TopQ, Q <: TopQ,
  +EM <: ConsHM[EG, EQ], +M <: ConsHM[G, Q],
] extends ConsHM[AddElementG[D, EP, P, EG, G], EQ | Q]

final abstract class CollectM[
  K <: TopK, D <: TopD, P >: BotVP <: TopP, B <: Bits,
  SG <: ChannelRefG[K, D, P], NG <: NtrlG[NatD[B]], SQ <: TopQ, NQ <: TopQ,
  +SM <: ConsHM[SG, SQ], +NM <: ConsHM[NG, NQ],
] extends ConsHM[CollectG[K, D, P, B, SG, NG], SQ | NQ]

final abstract class AddM[
  G <: ExprG[CollectedUD[NatUD]], Q <: TopQ,
  +M <: ConsHM[G, Q],
] extends ConsHM[AddG[G], Q]

final abstract class MultiplyM[
  G <: ExprG[CollectedUD[NatUD]], Q <: TopQ,
  +M <: ConsHM[G, Q],
] extends ConsHM[MultiplyG[G], Q]

final abstract class CompareM[
  G1 <: ExprG[NatUD], G2 <: ExprG[NatUD], Q1 <: TopQ, Q2 <: TopQ,
  +M1 <: ConsHM[G1, Q1], +M2 <: ConsHM[G2, Q2],
] extends ConsHM[CompareG[G1, G2], Q1 | Q2]

final abstract class PairM[
  D1 <: TopD, D2 <: TopD, P1 <: TopP, P2 <: TopP,
  G1 <: PolarG[D1, P1], G2 <: PolarG[D2, P2], Q1 <: TopQ, Q2 <: TopQ,
  +M1 <: ConsHM[G1, Q1], +M2 <: ConsHM[G2, Q2],
] extends ConsHM[PairG[D1, D2, P1, P2, G1, G2], Q1 | Q2]
