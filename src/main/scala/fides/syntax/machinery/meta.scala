package fides.syntax.machinery

import typelevelnumbers.binary.Bits

/**
  * Parent type of all the Scala types that represent
  * the different types of possible Fides code,
  * including the full metaprogramming landscape (aka <i>scapes</i>)
  */
trait ConsM[+G <: TopG] private[syntax]() // todo seal
// todo maybe Q should not be in here at all, but rather in Code
type TopM = ConsM[TopG]

/**
  * Helper type that is invariant in [[G]].
  */
private[syntax] sealed trait ConsHM[G <: TopG] extends ConsM[G]

final abstract class QuoteM[
  K <: TopK, KQ <: TopQ, P <: TopP, Q <: TopQ, T2M <: TopM, T1M <: ConsM[TopG],
  +KM <: ConsM[NameG[K]], +M <: ConsM[TopG],
] extends ConsHM[QuoteG[P, T2M, K, T1M]]

final abstract class EscapeM[
  TG <: TopG,
  K <: TopK, KQ <: TopQ, TM <: ConsHM[TG], P <: TopP, Q <: TopQ,
  // todo P and Q are loose, as I suspect they are in many places.
  //  Is that an issue? We could tighten them with helper types.
  +KM <: ConsM[NameG[K]], +M <: ConsM[PolarG[QuoteD[TM], P]],
] extends ConsHM[TG]

final abstract class ArgsM[
  E <: TopE, G <: TopG, Q <: TopQ,
  +M <: ConsHM[G],
] extends ConsHM[ArgsG[E, G]]

final abstract class ZipM[
  EG <: TopG, EQ <: TopQ,
  E <: TopE, EM <: ConsHM[EG], P <: TopP,
  G <: PolarG[CollectedD[E, QuoteD[EM]], P], Q <: TopQ,
  +M <: ConsHM[G],
] extends ConsHM[ZipG[EG, EQ, E, EM, P, G]]

final abstract class WrapM[
  D <: TopD,
  G <: ExprHG[D], Q <: TopQ,
  +M <: ConsHM[G],
] extends ConsHM[WrapG[D, G]]

final abstract class EvalM[
  D <: TopD, QQ <: TopQ,
  G <: ExprG[QuoteD[ConsM[ExprHG[D]]]], Q <: TopQ,
  +M <: ConsHM[G],
] extends ConsHM[EvalG[D, QQ, G]]
// todo Qs

final abstract class RepeatedM[
  G <: AplrG, Q <: TopQ,
  +M <: ConsHM[G],
] extends ConsHM[RepeatedG[G]]

final abstract class ConcurrentM[
  G <: ArgsUG[AplrG], Q <: TopQ,
  +M <: ConsHM[G],
] extends ConsHM[ConcurrentG[G]]

final abstract class ConjoinM[
  G <: ExprG[CollectedUD[BoolD]], Q <: TopQ,
  +M <: ConsHM[G],
] extends ConsHM[ConjoinG[G]]

final abstract class DisjoinM[
  G <: ExprG[CollectedUD[BoolD]], Q <: TopQ,
  +M <: ConsHM[G],
] extends ConsHM[DisjoinG[G]]

final abstract class NegateM[
  D <: BoolD, P <: TopP,
  G <: PolarG[D, P], Q <: TopQ,
  +M <: ConsHM[G],
] extends ConsHM[NegateG[D, P, G]]

final abstract class EqualM[
  G <: ExprG[CollectedUD[AtomD]], Q <: TopQ,
  +M <: ConsHM[G],
] extends ConsHM[EqualG[G]]

final abstract class RandomBitM[
  Q <: TopQ,
  +M <: ConsM[RandomBitG],
] extends ConsHM[RandomBitG]

final abstract class CollectedM[
  D <: TopD, P <: TopP,
  E <: TopE, EG <: PolarG[D, P],
  G <: ArgsG[E, EG], Q <: TopQ,
  +M <: ConsHM[G],
] extends ConsHM[CollectedG[D, P, E, EG, G]]

final abstract class AddElementM[
  D <: TopD, EP <: TopP, P <: TopP,
  EG <: PolarG[D, EP], G <: PolarG[CollectedUD[D], P], EQ <: TopQ, Q <: TopQ,
  +EM <: ConsHM[EG], +M <: ConsHM[G],
] extends ConsHM[AddElementG[D, EP, P, EG, G]]

final abstract class CollectM[
  K <: TopK, D <: TopD, P >: BotVP <: TopP, B <: Bits,
  SG <: ChanRefG[K, D, P], NG <: NtrlG[NatD[B]], SQ <: TopQ, NQ <: TopQ,
  +SM <: ConsHM[SG], +NM <: ConsHM[NG],
] extends ConsHM[CollectG[K, D, P, B, SG, NG]]

final abstract class AddM[
  G <: ExprG[CollectedUD[NatUD]], Q <: TopQ,
  +M <: ConsHM[G],
] extends ConsHM[AddG[G]]

final abstract class MultiplyM[
  G <: ExprG[CollectedUD[NatUD]], Q <: TopQ,
  +M <: ConsHM[G],
] extends ConsHM[MultiplyG[G]]

final abstract class CompareM[
  G1 <: ExprG[NatUD], G2 <: ExprG[NatUD], Q1 <: TopQ, Q2 <: TopQ,
  +M1 <: ConsHM[G1], +M2 <: ConsHM[G2],
] extends ConsHM[CompareG[G1, G2]]

final abstract class PairM[
  D1 <: TopD, D2 <: TopD, P1 <: TopP, P2 <: TopP,
  G1 <: PolarG[D1, P1], G2 <: PolarG[D2, P2], Q1 <: TopQ, Q2 <: TopQ,
  +M1 <: ConsHM[G1], +M2 <: ConsHM[G2],
] extends ConsHM[PairG[D1, D2, P1, P2, G1, G2]]
