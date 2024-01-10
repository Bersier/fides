package fides2024.syntax.polar

import fides2024.syntax.values.Integer
import fides2024.syntax.{Code, Expr}

/**
  * Outputs the sum of the two inputs.
  */
final case class Add(augend: Code[Expr[Integer]], addend: Code[Expr[Integer]]) extends Expr[Integer]

/**
  * Outputs the difference of the two inputs.
  */
final case class Subtract(minuend: Code[Expr[Integer]], subtrahend: Code[Expr[Integer]]) extends Expr[Integer]

/**
  * Outputs the negation of the input.
  */
final case class Negate(integer: Code[Expr[Integer]]) extends Expr[Integer]

/**
  * Outputs the product of the two inputs.
  */
final case class Multiply(multiplicand: Code[Expr[Integer]], multiplier: Code[Expr[Integer]]) extends Expr[Integer]

/**
  * Outputs the quotient of the two inputs.
  */
final case class Divide(dividend: Code[Expr[Integer]], divisor: Code[Expr[Integer]]) extends Expr[Integer]

/**
  * Outputs the remainder of the division of the two inputs.
  */
final case class Reduce(dividend: Code[Expr[Integer]], divisor: Code[Expr[Integer]]) extends Expr[Integer]