package fides.syntax.values

import fides.syntax.code.{Atom, Code, Expr, ValQ}

/**
  * Integer values
  */
sealed class WholeNumber(val value: BigInt) extends Atom, ValQ[WholeNumber]

/**
  * Natural number values
  */
final class NaturalNumber(value: BigInt) extends WholeNumber(value), ValQ[NaturalNumber]:
  assert(value >= 0)
end NaturalNumber

/**
  * Outputs the sum of the two inputs.
  */
final case class Add[N <: WholeNumber](augend: Code[Expr[N]], addend: Code[Expr[N]]) extends Expr[N]

/**
  * Outputs the difference of the two inputs.
  */
final case class Subtract(minuend: Code[Expr[WholeNumber]], subtrahend: Code[Expr[WholeNumber]]) extends Expr[WholeNumber]

/**
  * Outputs the negation of the input.
  */
final case class Negate(integer: Code[Expr[WholeNumber]]) extends Expr[WholeNumber]

/**
  * Outputs the product of the two inputs.
  */
final case class Multiply[N <: WholeNumber](multiplicand: Code[Expr[N]], multiplier: Code[Expr[N]]) extends Expr[N]

/**
  * Outputs the quotient of the two inputs.
  */
final case class Divide[N <: WholeNumber](dividend: Code[Expr[N]], divisor: Code[Expr[N]]) extends Expr[N]

/**
  * Outputs the remainder of the division of the two inputs.
  */
final case class Reduce[N <: WholeNumber](dividend: Code[Expr[N]], divisor: Code[Expr[N]]) extends Expr[N]

/**
  * Outputs -1 if the lhs is larger, 0 if they are equal, 1 if the rhs is larger.
  */
final case class Compare(lhs: Code[Expr[WholeNumber]], rhs: Code[Expr[WholeNumber]]) extends Expr[WholeNumber]
