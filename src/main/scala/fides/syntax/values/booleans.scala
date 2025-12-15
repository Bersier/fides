package fides.syntax.values

import fides.syntax.machinery.*

case object True extends Code[GenM[NtrlG[TrueD]]]
case object False extends Code[GenM[NtrlG[FalseD]]]

/**
  * Outputs the conjunction of the inputs.
  */
final case class Conjoin[
  G <: ExprG[CollectedUD[BoolD]],
  M <: GenHM[G],
](conjuncts: Code[M]) extends Code[ConjoinM[G, M]]

/**
  * Outputs the disjunction of the inputs.
  */
final case class Disjoin[
  G <: ExprG[CollectedUD[BoolD]],
  M <: GenHM[G],
](conjuncts: Code[M]) extends Code[DisjoinM[G, M]]

/**
  * Outputs the negation of the input.
  */
final case class Negate[
  D <: BoolD, P <: TopP,
  G <: PolarG[D, P],
  M <: GenHM[G],
](value: Code[M]) extends Code[NegateM[D, P, G, M]]

/**
  * Outputs true iff the atoms are the same.
  */
final case class Equal[
  G <: ExprG[CollectedUD[AtomD]],
  M <: GenHM[G],
](args: Code[M]) extends Code[EqualM[G, M]]
// todo not sure any atoms should be comparable... or perhaps it should be EqualByID?

/**
  * Outputs True with probability 1/2, False with probability 1/2.
  */
final case class RandomBit[
  M <: GenM[RandomBitG],
]() extends Code[RandomBitM[M]]
