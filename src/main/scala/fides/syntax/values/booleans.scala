package fides.syntax.values

import fides.syntax.types.*

case object True extends OldCode[Ntrl[TrueT]]
case object False extends OldCode[Ntrl[FalseT]]

/**
  * Outputs the conjunction of the inputs.
  */
final case class Conjoin(conjuncts: OldCode[ArgsUS[Expr[BoolT]]]) extends OldCode[Exvr[BoolT]]

/**
  * Outputs the disjunction of the inputs.
  */
final case class Disjoin(disjuncts: OldCode[ArgsUS[Expr[BoolT]]]) extends OldCode[Exvr[BoolT]]

/**
  * Outputs the negation of the input.
  */
final case class NegateBool(value: OldCode[Expr[BoolT]]) extends OldCode[Exvr[BoolT]]

/**
  * Outputs true iff the atoms are the same.
  */
final case class Equal(args: OldCode[ArgsUS[Expr[AtomT]]]) extends OldCode[Exvr[BoolT]]

/**
  * Outputs True with probability 1/2, False with probability 1/2.
  */
final case class RandomBit() extends OldCode[Exvr[BoolT]]
