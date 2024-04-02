package fides.syntax.values

import fides.syntax.code.{Atom, Code, Expr, Val}

/**
  * Boolean values
  */
sealed trait Bool extends Atom, Val[Bool]
case object True extends Bool
case object False extends Bool

/**
  * Outputs the conjunction of the two inputs.
  */
final case class Conjoin(first: Code[Expr[Bool]], second: Code[Expr[Bool]]) extends Expr[Bool]

/**
  * Outputs the disjunction of the two inputs.
  */
final case class Disjoin(first: Code[Expr[Bool]], second: Code[Expr[Bool]]) extends Expr[Bool]

/**
  * Outputs the negation of the input.
  */
final case class NegateBool(value: Code[Expr[Bool]]) extends Expr[Bool]

/**
  * Outputs true iff the two atoms are the same.
  */
final case class Equal(first: Code[Expr[Atom]], second: Code[Expr[Atom]]) extends Expr[Bool]

/**
  * Outputs True with probability 1/2, False with probability 1/2.
  */
final case class RandomBit() extends Expr[Bool]
