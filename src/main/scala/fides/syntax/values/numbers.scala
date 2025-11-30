package fides.syntax.values

import fides.syntax.core.Code
import fides.syntax.types.{CollectedUT, Expr, Exvr, NatT, NatUT, Ntrl}
import typelevelnumbers.binary.Bits

import scala.annotation.publicInBinary

/**
  * Natural number values
  */
final case class Nat[B <: Bits] @publicInBinary private(bits: B) extends Code[Ntrl[NatT[B]]]:
  assert(bits.withoutTrailingZeros == bits)
  override def toString: String = bits.toBigInt.toString
object Nat:
  def apply[B <: Bits & Singleton](bits: B): Nat[B] = new Nat(bits)
  inline def from[N <: Int & Singleton](n: N): Nat[Bits.FromInt[N]] = new Nat(Bits.fromInt(n))
end Nat

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
