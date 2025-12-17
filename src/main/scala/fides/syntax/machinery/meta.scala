package fides.syntax.machinery

import typelevelnumbers.binary.Bits

/**
  * Parent type of all the Scala types that represent
  * the different types of possible Fides code,
  * including the full metaprogramming landscape (aka <i>scapes</i>)
  */
trait GenM[+G <: TopG] private[syntax]() // todo seal
type TopM = GenM[TopG]
// todo make sure we have the correct subtyping relation for Ds, Gs, and Ms.
//  Looking at Quote and Escape can help find it.

sealed abstract class Gen2M[+G <: TopG] private[machinery](g: G)

/**
  * Helper type that is invariant in [[G]].
  */
private[syntax] sealed trait GenHM[G <: TopG] extends GenM[G]

final abstract class QuoteM[
  K <: TopK, P <: TopP, T2M <: TopM, T1M <: TopM,
  +KM <: GenHM[NameG[K]], +M <: TopM,
] extends GenHM[QuoteG[P, T2M, K, T1M]]

final abstract class EscapeM[
  SG <: TopG,
  SM <: GenHM[SG],
  +KM <: GenHM[NameG[TopK]], +M <: GenM[PolarG[QuoteD[SM], TopP]],
] extends GenHM[SG]

final abstract class ArgsM[
  E <: TopE, G <: TopG,
  +M <: GenHM[G],
] extends GenHM[ArgsG[E, G]]

final abstract class ZipM[
  EG <: TopG,
  E <: TopE, EM <: GenHM[EG], P <: TopP,
  G <: PolarG[CollectedD[E, QuoteD[EM]], P],
  +M <: GenHM[G],
] extends GenHM[ZipG[EG, E, EM, P, G]]

final abstract class WrapM[
  D <: TopD,
  G <: ExprHG[D],
  +M <: GenHM[G],
] extends GenHM[WrapG[D, G]]

final abstract class EvalM[
  D <: TopD,
  G <: ExprG[QuoteD[GenM[ExprHG[D]]]],
  +M <: GenHM[G],
] extends GenHM[EvalG[D, G]]

final abstract class RepeatedM[
  G <: AplrG,
  +M <: GenHM[G],
] extends GenHM[RepeatedG[G]]

final abstract class ConcurrentM[
  G <: ArgsUG[AplrG],
  +M <: GenHM[G],
] extends GenHM[ConcurrentG[G]]

final abstract class ConjoinM[
  G <: ExprG[CollectedUD[BoolD]],
  +M <: GenHM[G],
] extends GenHM[ConjoinG[G]]

final abstract class DisjoinM[
  G <: ExprG[CollectedUD[BoolD]],
  +M <: GenHM[G],
] extends GenHM[DisjoinG[G]]

final abstract class NegateM[
  D <: BoolD, P <: TopP,
  G <: PolarG[D, P],
  +M <: GenHM[G],
] extends GenHM[NegateG[D, P, G]]

final abstract class EqualM[
  G <: ExprG[CollectedUD[AtomD]],
  +M <: GenHM[G],
] extends GenHM[EqualG[G]]

final abstract class RandomBitM[
  +M <: GenM[RandomBitG],
] extends GenHM[RandomBitG]

final abstract class CollectedM[
  D <: TopD, P <: TopP,
  E <: TopE, EG <: PolarG[D, P],
  G <: ArgsG[E, EG],
  +M <: GenHM[G],
] extends GenHM[CollectedG[D, P, E, EG, G]]

final abstract class AddElementM[
  D <: TopD, EP <: TopP, P <: TopP,
  EG <: PolarG[D, EP], G <: PolarG[CollectedUD[D], P],
  +EM <: GenHM[EG], +M <: GenHM[G],
] extends GenHM[AddElementG[D, EP, P, EG, G]]

final abstract class CollectM[
  K <: TopK, D <: TopD, P >: BotVP <: TopP, B <: Bits,
  SG <: ChanRefG[K, D, P], NG <: NtrlG[NatD[B]],
  +SM <: GenHM[SG], +NM <: GenHM[NG],
] extends GenHM[CollectG[K, D, P, B, SG, NG]]

final abstract class AddM[
  G <: ExprG[CollectedUD[NatUD]],
  +M <: GenHM[G],
] extends GenHM[AddG[G]]

final abstract class MultiplyM[
  G <: ExprG[CollectedUD[NatUD]],
  +M <: GenHM[G],
] extends GenHM[MultiplyG[G]]

final abstract class CompareM[
  G1 <: ExprG[NatUD], G2 <: ExprG[NatUD],
  +M1 <: GenHM[G1], +M2 <: GenHM[G2],
] extends GenHM[CompareG[G1, G2]]

final abstract class PairM[
  D1 <: TopD, D2 <: TopD, P1 <: TopP, P2 <: TopP,
  G1 <: PolarG[D1, P1], G2 <: PolarG[D2, P2],
  +M1 <: GenHM[G1], +M2 <: GenHM[G2],
] extends GenHM[PairG[D1, D2, P1, P2, G1, G2]]
