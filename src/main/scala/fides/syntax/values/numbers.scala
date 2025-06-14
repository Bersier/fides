package fides.syntax.values

import fides.syntax.core.Code
import fides.syntax.types.{Args, Expr, NaturalNumberT, Ntrl, WholeNumberT}

/**
  * Integer values
  */
sealed class WholeNumber(val value: BigInt) extends Code[Ntrl[WholeNumberT]]:
  override def toString: String = value.toString()
end WholeNumber

/**
  * Natural number values
  */
final class NaturalNumber(value: BigInt) extends Code[Ntrl[NaturalNumberT]]:
  assert(value >= 0)
end NaturalNumber

/**
  * Outputs the sum of the inputs.
  */
final case class Add[N <: WholeNumberT](terms: Code[Args[Expr[N]]]) extends Code[Expr[N]]

/**
  * Outputs the negation of the input.
  */
final case class Negate(integer: Code[Expr[WholeNumberT]]) extends Code[Expr[WholeNumberT]]

/**
  * Outputs the product of the inputs.
  */
final case class Multiply[N <: WholeNumberT](factors: Code[Args[Expr[N]]]) extends Code[Expr[N]]

/**
  * Outputs the quotient of the two inputs.
  */
final case class Divide[N <: WholeNumberT](dividend: Code[Expr[N]], divisor: Code[Expr[N]]) extends Code[Expr[N]]

/**
  * Outputs the remainder of the division of the two inputs.
  */
final case class Reduce[N <: WholeNumberT](dividend: Code[Expr[N]], divisor: Code[Expr[N]]) extends Code[Expr[N]]

/**
  * Outputs -1 if the lhs is larger, 0 if they are equal, 1 if the rhs is larger.
  */
final case class Compare(lhs: Code[Expr[WholeNumberT]], rhs: Code[Expr[WholeNumberT]]) extends Code[Expr[WholeNumberT]]
