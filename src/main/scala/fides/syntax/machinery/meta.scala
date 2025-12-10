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
  K <: TopK, P <: TopP, T2M <: TopM, T1M <: ConsM[TopG],
  +KM <: ConsM[NameG[K]], +M <: ConsM[TopG],
] extends ConsHM[QuoteG[P, T2M, K, T1M]]

final abstract class EscapeM[
  TG <: TopG,
  K <: TopK, TM <: ConsHM[TG],
  // todo P and Q are loose, as I suspect they are in many places.
  //  Is that an issue? We could tighten them with helper types.
  +KM <: ConsM[NameG[K]], +M <: ConsM[PolarG[QuoteD[TM], TopP]],
] extends ConsHM[TG]

final abstract class ArgsM[
  E <: TopE, G <: TopG,
  +M <: ConsHM[G],
] extends ConsHM[ArgsG[E, G]]

final abstract class ZipM[
  EG <: TopG,
  E <: TopE, EM <: ConsHM[EG], P <: TopP,
  G <: PolarG[CollectedD[E, QuoteD[EM]], P],
  +M <: ConsHM[G],
] extends ConsHM[ZipG[EG, E, EM, P, G]]

final abstract class WrapM[
  D <: TopD,
  G <: ExprHG[D],
  +M <: ConsHM[G],
] extends ConsHM[WrapG[D, G]]

final abstract class EvalM[
  D <: TopD,
  G <: ExprG[QuoteD[ConsM[ExprHG[D]]]],
  +M <: ConsHM[G],
] extends ConsHM[EvalG[D, G]]
// todo Qs

final abstract class RepeatedM[
  G <: AplrG,
  +M <: ConsHM[G],
] extends ConsHM[RepeatedG[G]]

final abstract class ConcurrentM[
  G <: ArgsUG[AplrG],
  +M <: ConsHM[G],
] extends ConsHM[ConcurrentG[G]]

final abstract class ConjoinM[
  G <: ExprG[CollectedUD[BoolD]],
  +M <: ConsHM[G],
] extends ConsHM[ConjoinG[G]]

final abstract class DisjoinM[
  G <: ExprG[CollectedUD[BoolD]],
  +M <: ConsHM[G],
] extends ConsHM[DisjoinG[G]]

final abstract class NegateM[
  D <: BoolD, P <: TopP,
  G <: PolarG[D, P],
  +M <: ConsHM[G],
] extends ConsHM[NegateG[D, P, G]]

final abstract class EqualM[
  G <: ExprG[CollectedUD[AtomD]],
  +M <: ConsHM[G],
] extends ConsHM[EqualG[G]]

final abstract class RandomBitM[
  +M <: ConsM[RandomBitG],
] extends ConsHM[RandomBitG]

final abstract class CollectedM[
  D <: TopD, P <: TopP,
  E <: TopE, EG <: PolarG[D, P],
  G <: ArgsG[E, EG],
  +M <: ConsHM[G],
] extends ConsHM[CollectedG[D, P, E, EG, G]]

final abstract class AddElementM[
  D <: TopD, EP <: TopP, P <: TopP,
  EG <: PolarG[D, EP], G <: PolarG[CollectedUD[D], P],
  +EM <: ConsHM[EG], +M <: ConsHM[G],
] extends ConsHM[AddElementG[D, EP, P, EG, G]]

final abstract class CollectM[
  K <: TopK, D <: TopD, P >: BotVP <: TopP, B <: Bits,
  SG <: ChanRefG[K, D, P], NG <: NtrlG[NatD[B]],
  +SM <: ConsHM[SG], +NM <: ConsHM[NG],
] extends ConsHM[CollectG[K, D, P, B, SG, NG]]

final abstract class AddM[
  G <: ExprG[CollectedUD[NatUD]],
  +M <: ConsHM[G],
] extends ConsHM[AddG[G]]

final abstract class MultiplyM[
  G <: ExprG[CollectedUD[NatUD]],
  +M <: ConsHM[G],
] extends ConsHM[MultiplyG[G]]

final abstract class CompareM[
  G1 <: ExprG[NatUD], G2 <: ExprG[NatUD],
  +M1 <: ConsHM[G1], +M2 <: ConsHM[G2],
] extends ConsHM[CompareG[G1, G2]]

final abstract class PairM[
  `D1+` <: TopD, D1 <: `D1+`, `D1-` <: D1, `D2+` <: TopD, D2 <: `D2+`, `D2-` <: D2, P1 <: TopP, P2 <: TopP,
  G1 <: Polar2G[`D1+`, D1, `D1-`, P1], G2 <: Polar2G[`D2+`, D2, `D2-`, P2],
  +M1 <: ConsHM[G1], +M2 <: ConsHM[G2],
] extends ConsHM[PairG[`D1+`, D1, `D1-`, `D2+`, D2, `D2-`, P1, P2, G1, G2]]
