package fides.syntax.values

import fides.syntax.types.{ArgsUS, AtomT, BoolT, Code, Expr, Exvr, FalseT, Ntrl, TrueT}

case object True extends Code[Ntrl[TrueT]]
case object False extends Code[Ntrl[FalseT]]

/**
  * Outputs the conjunction of the inputs.
  */
final case class Conjoin(conjuncts: Code[ArgsUS[Expr[BoolT]]]) extends Code[Exvr[BoolT]]

/**
  * Outputs the disjunction of the inputs.
  */
final case class Disjoin(disjuncts: Code[ArgsUS[Expr[BoolT]]]) extends Code[Exvr[BoolT]]

/**
  * Outputs the negation of the input.
  */
final case class NegateBool(value: Code[Expr[BoolT]]) extends Code[Exvr[BoolT]]

/**
  * Outputs true iff the atoms are the same.
  */
final case class Equal(args: Code[ArgsUS[Expr[AtomT]]]) extends Code[Exvr[BoolT]]

/**
  * Outputs True with probability 1/2, False with probability 1/2.
  */
final case class RandomBit() extends Code[Exvr[BoolT]]
