package fides.syntax.values

import fides.syntax.types.*
import typelevelnumbers.binary.Bits

import scala.annotation.publicInBinary

/**
  * Natural number values
  */
final case class NatLit[B <: Bits] @publicInBinary private(bits: B) extends Code[ConsM[Ntrl2G[NatD[B]], BotQ]]:
  assert(bits.withoutTrailingZeros == bits)
  override def toString: String = bits.toBigInt.toString
object NatLit:
  def apply[B <: Bits & Singleton](bits: B): NatLit[B] = new NatLit(bits)
  inline def from[N <: Int & Singleton](n: N): NatLit[Bits.FromInt[N]] = new NatLit(Bits.fromInt(n))
end NatLit

/**
  * Outputs the sum of the inputs.
  */
final case class Add[
  G <: Expr2G[CollectedUD[NatUD]], Q <: TopQ,
  M <: ConsM[G, Q],
](terms: Code[M]) extends Code[AddM[G, Q, M]]

/**
  * Outputs the product of the inputs.
  */
final case class Multiply[
  G <: Expr2G[CollectedUD[NatUD]], Q <: TopQ,
  M <: ConsM[G, Q],
](factors: Code[M]) extends Code[MultiplyM[G, Q, M]]

/**
  * Outputs -1 if the lhs is larger, 0 if they are equal, 1 if the rhs is larger.
  */
final case class Compare[
  G1 <: Expr2G[NatUD], G2 <: Expr2G[NatUD], Q <: TopQ,
  M1 <: ConsM[G1, Q], M2 <: ConsM[G2, Q],
](lhs: Code[M1], rhs: Code[M2]) extends Code[CompareM[G1, G2, Q, M1, M2]]
// todo return a trit instead? -1 is not even an option anymore
