package fides.syntax.values

import fides.syntax.core.Code
import fides.syntax.types.{AtomT, Expr, Lit, Ntrl}
import fides.syntax.meta.Args

sealed trait BoolT extends AtomT
sealed trait TrueT extends BoolT
sealed trait FalseT extends BoolT

case object True extends Lit, Ntrl[TrueT]
case object False extends Lit, Ntrl[FalseT]

/**
  * Outputs the conjunction of the inputs.
  */
final case class Conjoin(conjuncts: Code[Args[Expr[BoolT]]]) extends Expr[BoolT]

/**
  * Outputs the disjunction of the inputs.
  */
final case class Disjoin(disjuncts: Code[Args[Expr[BoolT]]]) extends Expr[BoolT]

/**
  * Outputs the negation of the input.
  */
final case class NegateBool(value: Code[Expr[BoolT]]) extends Expr[BoolT]

/**
  * Outputs true iff the atoms are the same.
  */
final case class Equal(args: Code[Args[Expr[AtomT]]]) extends Expr[BoolT]

/**
  * Outputs True with probability 1/2, False with probability 1/2.
  */
final case class RandomBit() extends Expr[BoolT]
