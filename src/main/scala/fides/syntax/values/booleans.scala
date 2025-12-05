package fides.syntax.values

import fides.syntax.types.*

case object True extends OldCode[NtrlS[TrueD]]
case object False extends OldCode[NtrlS[FalseD]]

/**
  * Outputs the conjunction of the inputs.
  */
final case class Conjoin(conjuncts: OldCode[ArgsUS[ExprS[BoolD]]]) extends OldCode[ExvrS[BoolD]]

/**
  * Outputs the disjunction of the inputs.
  */
final case class Disjoin(disjuncts: OldCode[ArgsUS[ExprS[BoolD]]]) extends OldCode[ExvrS[BoolD]]

/**
  * Outputs the negation of the input.
  */
final case class NegateBool(value: OldCode[ExprS[BoolD]]) extends OldCode[ExvrS[BoolD]]

/**
  * Outputs true iff the atoms are the same.
  */
final case class Equal(args: OldCode[ArgsUS[ExprS[AtomD]]]) extends OldCode[ExvrS[BoolD]]

/**
  * Outputs True with probability 1/2, False with probability 1/2.
  */
final case class RandomBit() extends OldCode[ExvrS[BoolD]]
