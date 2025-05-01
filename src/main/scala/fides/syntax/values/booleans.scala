package fides.syntax.values

import fides.syntax.core.Code
import fides.syntax.types.{Args, AtomT, BoolT, Expr, FalseT, Lit, Ntrl, TrueT}
import fides.syntax.meta.Args

case object True extends Code[Lit & Ntrl[TrueT]]
case object False extends Code[Lit & Ntrl[FalseT]]

/**
  * Outputs the conjunction of the inputs.
  */
final case class Conjoin(conjuncts: Code[Args[Expr[BoolT]]]) extends Code[Expr[BoolT]]

/**
  * Outputs the disjunction of the inputs.
  */
final case class Disjoin(disjuncts: Code[Args[Expr[BoolT]]]) extends Code[Expr[BoolT]]

/**
  * Outputs the negation of the input.
  */
final case class NegateBool(value: Code[Expr[BoolT]]) extends Code[Expr[BoolT]]

/**
  * Outputs true iff the atoms are the same.
  */
final case class Equal(args: Code[Args[Expr[AtomT]]]) extends Code[Expr[BoolT]]

/**
  * Outputs True with probability 1/2, False with probability 1/2.
  */
final case class RandomBit() extends Code[Expr[BoolT]]
