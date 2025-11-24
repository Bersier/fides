package fides.syntax.values

import fides.syntax.core.Code
import fides.syntax.types.{CollectedUT, Expr, Exvr, NatBT, NatT, Ntrl}
import typelevelnumbers.binary.Bits

/**
  * Natural number values
  */
final case class NaturalNumber[B <: Bits](bits: B) extends Code[Ntrl[NatBT[B]]]:
  assert(bits.withoutTrailingZeros == bits)
  override def toString: String = bits.toBigInt.toString
end NaturalNumber

/**
  * Outputs the sum of the inputs.
  */
final case class Add(terms: Code[Expr[CollectedUT[NatT]]]) extends Code[Exvr[NatT]]

/**
  * Outputs the product of the inputs.
  */
final case class Multiply(factors: Code[Expr[CollectedUT[NatT]]]) extends Code[Exvr[NatT]]

/**
  * Outputs -1 if the lhs is larger, 0 if they are equal, 1 if the rhs is larger.
  */
final case class Compare(lhs: Code[Expr[NatT]], rhs: Code[Expr[NatT]]) extends Code[Exvr[NatT]]
