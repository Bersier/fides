package fides.syntax.machinery

import typelevelnumbers.binary.Bits

/**
  * Parent type of all the Scala types that represent
  * the different types of possible Fides code, including the full metaprogramming landscape
  */
trait ConsM[+G <: TopG, +Q <: TopQ] private[syntax]() // todo seal
type TopM = ConsM[TopG, TopQ]

/**
  * @tparam RM is actually supposed to be derived from M
  *            via the relation [[TrimmedR]]`[`[[G]], [[M]], [[RM]]`]` (See [[Quote]])
  */
final abstract class QuoteM[
  G <: TopG, P <: TopP, Q <: TopQ,
  RM <: ConsM[G, ConsQ[P, BotQ]],
  +M <: ConsM[G, ConsQ[P, Q]],
] extends ConsM[QuoteG[G, P, RM], Q]

sealed trait EscapeM[+G <: TopG, +Q <: TopQ] extends ConsM[G, Q]
object EscapeM:
  final abstract class Head[
    G <: TopG, P <: TopP, Q <: TopQ,
    +M <: ConsM[PolarG[QuoteD[G], P], Q],
  ] extends EscapeM[G, ConsQ[P | GenP[BotB, BotB, TopB], Q]]

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
  D <: TopD, B <: Bits,
  SG <: NtrlG[ChanD[?, ?]], NG <: NtrlG[NatD[B]], SQ <: TopQ, NQ <: TopQ,
  +SM <: ConsM[SG, SQ], +NM <: ConsM[NG, NQ],
] extends ConsM[CollectG[D, B, SG, NG], SQ | NQ]

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
