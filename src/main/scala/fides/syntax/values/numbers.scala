package fides.syntax.values

import fides.syntax.types.*
import typelevelnumbers.binary.Bits

import scala.annotation.publicInBinary

/**
  * Natural number values
  */
final case class Nat[B <: Bits] @publicInBinary private(bits: B) extends Code[Ntrl2[NatT[B]], BotM]:
  assert(bits.withoutTrailingZeros == bits)
  override def toString: String = bits.toBigInt.toString
object Nat:
  def apply[B <: Bits & Singleton](bits: B): Nat[B] = new Nat(bits)
  inline def from[N <: Int & Singleton](n: N): Nat[Bits.FromInt[N]] = new Nat(Bits.fromInt(n))
end Nat

/**
  * Outputs the sum of the inputs.
  */
final case class Add[M <: TopM](terms: Code[Expr2[CollectedUT[NatUT]], M]) extends Code[Expr2[NatUT], M]

/**
  * Outputs the product of the inputs.
  */
final case class Multiply[M <: TopM](factors: Code[Expr[CollectedUT[NatUT]], M]) extends Code[Expr2[NatUT], M]

/**
  * Outputs -1 if the lhs is larger, 0 if they are equal, 1 if the rhs is larger.
  */
final case class Compare[M <: TopM](
  lhs: Code[Expr2[NatUT], M],
  rhs: Code[Expr2[NatUT], M],
) extends Code[Expr2[NatUT], M]
// todo return a trit instead?
