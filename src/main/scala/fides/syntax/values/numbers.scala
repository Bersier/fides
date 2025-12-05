package fides.syntax.values

import fides.syntax.types.*
import typelevelnumbers.binary.Bits

import scala.annotation.publicInBinary

/**
  * Natural number values
  */
final case class Nat[B <: Bits] @publicInBinary private(bits: B) extends ConsC[Ntrl2[NatD[B]], BotQ]:
  assert(bits.withoutTrailingZeros == bits)
  override def toString: String = bits.toBigInt.toString
object Nat:
  def apply[B <: Bits & Singleton](bits: B): Nat[B] = new Nat(bits)
  inline def from[N <: Int & Singleton](n: N): Nat[Bits.FromInt[N]] = new Nat(Bits.fromInt(n))
end Nat

/**
  * Outputs the sum of the inputs.
  */
final case class Add[Q <: TopQ](terms: ConsC[Expr2[CollectedUD[NatUD]], Q]) extends ConsC[Expr2[NatUD], Q]

/**
  * Outputs the product of the inputs.
  */
final case class Multiply[Q <: TopQ](factors: ConsC[Expr[CollectedUD[NatUD]], Q]) extends ConsC[Expr2[NatUD], Q]

/**
  * Outputs -1 if the lhs is larger, 0 if they are equal, 1 if the rhs is larger.
  */
final case class Compare[Q <: TopQ](
  lhs: ConsC[Expr2[NatUD], Q],
  rhs: ConsC[Expr2[NatUD], Q],
) extends ConsC[Expr2[NatUD], Q]
// todo return a trit instead?
