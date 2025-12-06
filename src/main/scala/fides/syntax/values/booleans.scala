package fides.syntax.values

import fides.syntax.machinery.*

case object True extends Code[ConsM[Ntrl2G[TrueD], BotQ]]
case object False extends Code[ConsM[Ntrl2G[FalseD], BotQ]]

/**
  * Outputs the conjunction of the inputs.
  */
final case class Conjoin[
  G <: Expr2G[CollectedUD[BoolD]], Q <: TopQ,
  M <: ConsM[G, Q],
](conjuncts: Code[M]) extends Code[ConjoinM[G, Q, M]]

/**
  * Outputs the disjunction of the inputs.
  */
final case class Disjoin[
  G <: Expr2G[CollectedUD[BoolD]], Q <: TopQ,
  M <: ConsM[G, Q],
](conjuncts: Code[M]) extends Code[DisjoinM[G, Q, M]]

/**
  * Outputs the negation of the input.
  */
final case class Negate[
  D <: BoolD, P <: TopP,
  G <: Polar2G[D, P], Q <: TopQ,
  M <: ConsM[G, Q],
](value: Code[M]) extends Code[NegateM[D, P, G, Q, M]]

/**
  * Outputs true iff the atoms are the same.
  */
final case class Equal[
  G <: Expr2G[CollectedUD[AtomD]], Q <: TopQ,
  M <: ConsM[G, Q],
](args: Code[M]) extends Code[EqualM[G, Q, M]]
// todo not sure any atoms should be comparable... or perhaps it should be EqualByID?

/**
  * Outputs True with probability 1/2, False with probability 1/2.
  */
final case class RandomBit[
  Q <: TopQ,
  M <: ConsM[RandomBitG, Q],
]() extends Code[RandomBitM[Q, M]]
