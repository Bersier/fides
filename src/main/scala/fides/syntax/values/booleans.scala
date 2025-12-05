package fides.syntax.values

import fides.syntax.types.*

case object True extends OldCode[Ntrl[TrueD]]
case object False extends OldCode[Ntrl[FalseD]]

/**
  * Outputs the conjunction of the inputs.
  */
final case class Conjoin(conjuncts: OldCode[ArgsUS[Expr[BoolD]]]) extends OldCode[Exvr[BoolD]]

/**
  * Outputs the disjunction of the inputs.
  */
final case class Disjoin(disjuncts: OldCode[ArgsUS[Expr[BoolD]]]) extends OldCode[Exvr[BoolD]]

/**
  * Outputs the negation of the input.
  */
final case class NegateBool(value: OldCode[Expr[BoolD]]) extends OldCode[Exvr[BoolD]]

/**
  * Outputs true iff the atoms are the same.
  */
final case class Equal(args: OldCode[ArgsUS[Expr[AtomD]]]) extends OldCode[Exvr[BoolD]]

/**
  * Outputs True with probability 1/2, False with probability 1/2.
  */
final case class RandomBit() extends OldCode[Exvr[BoolD]]
