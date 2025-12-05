package fides.syntax.values

import fides.syntax.types.*

case object True extends OldCode[NtrlG[TrueD]]
case object False extends OldCode[NtrlG[FalseD]]

/**
  * Outputs the conjunction of the inputs.
  */
final case class Conjoin(conjuncts: OldCode[ArgsUG[ExprG[BoolD]]]) extends OldCode[ExvrG[BoolD]]

/**
  * Outputs the disjunction of the inputs.
  */
final case class Disjoin(disjuncts: OldCode[ArgsUG[ExprG[BoolD]]]) extends OldCode[ExvrG[BoolD]]

/**
  * Outputs the negation of the input.
  */
final case class NegateBool(value: OldCode[ExprG[BoolD]]) extends OldCode[ExvrG[BoolD]]

/**
  * Outputs true iff the atoms are the same.
  */
final case class Equal(args: OldCode[ArgsUG[ExprG[AtomD]]]) extends OldCode[ExvrG[BoolD]]

/**
  * Outputs True with probability 1/2, False with probability 1/2.
  */
final case class RandomBit() extends OldCode[ExvrG[BoolD]]
