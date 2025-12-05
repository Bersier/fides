package fides.syntax.types

/**
  * Parent type of all the Scala types that represent
  * the different types of possible Fides code, including the full metaprogramming landscape
  */
trait ConsM[+G <: TopG, +Q <: TopQ] private[syntax]() // todo seal
type TopM = ConsM[TopG, TopQ]

final abstract class QuoteM[
  G <: TopG, P <: TopP, Q <: TopQ,
  RM <: ConsM[G, ConsQ[P, BotQ]],
  +M <: ConsM[G, ConsQ[P, Q]],
] extends ConsM[QuoteG[G, P, RM], Q]

sealed trait EscapeM[+G <: TopG, +Q <: TopQ] extends ConsM[G, Q]
object EscapeM:
  final abstract class Head[
    G <: TopG, P <: TopP, Q <: TopQ,
    +M <: ConsM[Polar2G[QuoteD[G], P], Q],
  ] extends EscapeM[G, ConsQ[P | GenP[BotB, BotB, TopB], Q]]

  final abstract class Step[
    G <: TopG, Q <: TopQ,
    +M <: EscapeM[G, Q],
  ] extends EscapeM[G, ConsQ[BotP, Q]]
end EscapeM

final abstract class ConjoinM[
  G <: Expr2G[CollectedUD[BoolD]], Q <: TopQ,
  +M <: ConsM[G, Q],
] extends ConsM[ConjoinS[G], Q]

final abstract class DisjoinM[
  G <: Expr2G[CollectedUD[BoolD]], Q <: TopQ,
  +M <: ConsM[G, Q],
] extends ConsM[DisjoinS[G], Q]

final abstract class NegateM[
  D <: BoolD, P <: TopP,
  G <: Polar2G[D, P], Q <: TopQ,
  +M <: ConsM[G, Q],
] extends ConsM[NegateS[D, P, G], Q]

final abstract class EqualM[
  G <: Expr2G[CollectedUD[AtomD]], Q <: TopQ,
  +M <: ConsM[G, Q],
] extends ConsM[EqualS[G], Q]

final abstract class RandomBitM[
  Q <: TopQ,
  +M <: ConsM[RandomBitS, Q],
] extends ConsM[RandomBitS, Q]

final abstract class AddM[
  G <: Expr2G[CollectedUD[NatUD]], Q <: TopQ,
  +M <: ConsM[G, Q],
] extends ConsM[AddG[G], Q]

final abstract class MultiplyM[
  G <: Expr2G[CollectedUD[NatUD]], Q <: TopQ,
  +M <: ConsM[G, Q],
] extends ConsM[MultiplyG[G], Q]

final abstract class CompareM[
  G1 <: Expr2G[NatUD], G2 <: Expr2G[NatUD], Q <: TopQ,
  +M1 <: ConsM[G1, Q], +M2 <: ConsM[G2, Q],
] extends ConsM[CompareG[G1, G2], Q]

final abstract class PairM[
  D1 <: TopD, D2 <: TopD, P <: TopP,
  G1 <: Polar2G[D1, P], G2 <: Polar2G[D2, P], Q <: TopQ,
  +M1 <: ConsM[G1, Q], +M2 <: ConsM[G2, Q],
] extends ConsM[PairG[D1, D2, P, G1, G2], Q]
