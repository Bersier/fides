package fides.syntax.values

import fides.syntax.machinery.*
import typelevelnumbers.binary.Bits

import scala.annotation.publicInBinary

/**
  * Natural number values
  */
final case class NatLit[B <: Bits] @publicInBinary private(bits: B) extends Code[ConsM[NtrlG[NatD[B]], BotQ]]:
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
  G <: ExprG[CollectedUD[NatUD]], Q <: TopQ,
  M <: ConsM[G, Q],
](terms: Code[M]) extends Code[AddM[G, Q, M]]

/**
  * Outputs the product of the inputs.
  */
final case class Multiply[
  G <: ExprG[CollectedUD[NatUD]], Q <: TopQ,
  M <: ConsM[G, Q],
](factors: Code[M]) extends Code[MultiplyM[G, Q, M]]

/**
  * Outputs [[True]] iff [[lhs]] is strictly smaller than [[rhs]].
  */
final case class Compare[
  G1 <: ExprG[NatUD], G2 <: ExprG[NatUD], Q1 <: TopQ, Q2 <: TopQ,
  M1 <: ConsM[G1, Q1], M2 <: ConsM[G2, Q2],
](lhs: Code[M1], rhs: Code[M2]) extends Code[CompareM[G1, G2, Q1, Q2, M1, M2]]
