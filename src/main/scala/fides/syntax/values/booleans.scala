package fides.syntax.values

import fides.syntax.code.{Atom, Code, Expr, Val}
import fides.syntax.meta.Args

/**
  * Boolean values
  */
sealed trait Bool extends Atom, Val[Bool]
case object True extends Bool
case object False extends Bool

/**
  * Outputs the conjunction of the inputs.
  */
final case class Conjoin(conjuncts: Code[Args[Expr[Bool]]]) extends Expr[Bool]

/**
  * Outputs the disjunction of the inputs.
  */
final case class Disjoin(disjuncts: Code[Args[Expr[Bool]]]) extends Expr[Bool]

/**
  * Outputs the negation of the input.
  */
final case class NegateBool(value: Code[Expr[Bool]]) extends Expr[Bool]

/**
  * Outputs true iff the atoms are the same.
  */
final case class Equal(args: Code[Args[Expr[Atom]]]) extends Expr[Bool]

/**
  * Outputs True with probability 1/2, False with probability 1/2.
  */
final case class RandomBit() extends Expr[Bool]
