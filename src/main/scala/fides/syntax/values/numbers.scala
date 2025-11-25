package fides.syntax.values

import fides.syntax.core.Code
import fides.syntax.types.{CollectedUT, Expr, Exvr, NatT, NatUT, Ntrl}
import typelevelnumbers.binary.Bits

import scala.annotation.publicInBinary

/**
  * Natural number values
  */
final case class NaturalNumber[B <: Bits] @publicInBinary private(bits: B) extends Code[Ntrl[NatT[B]]]:
  assert(bits.withoutTrailingZeros == bits)
  override def toString: String = bits.toBigInt.toString
object NaturalNumber:
  def apply[B <: Bits & Singleton](bits: B): NaturalNumber[B] = new NaturalNumber(bits)
  inline def from[N <: Int & Singleton](n: N): NaturalNumber[Bits.FromInt[N]] = new NaturalNumber(Bits.fromInt(n))
end NaturalNumber

/**
  * Outputs the sum of the inputs.
  */
final case class Add(terms: Code[Expr[CollectedUT[NatUT]]]) extends Code[Exvr[NatUT]]

/**
  * Outputs the product of the inputs.
  */
final case class Multiply(factors: Code[Expr[CollectedUT[NatUT]]]) extends Code[Exvr[NatUT]]

/**
  * Outputs -1 if the lhs is larger, 0 if they are equal, 1 if the rhs is larger.
  */
final case class Compare(lhs: Code[Expr[NatUT]], rhs: Code[Expr[NatUT]]) extends Code[Exvr[NatUT]]
