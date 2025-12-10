package fides.syntax.values

import fides.syntax.machinery.*
import typelevelnumbers.binary.Bits

import scala.annotation.publicInBinary

/**
  * Natural number values
  */
final case class NatLit[B <: Bits] @publicInBinary private(bits: B) extends Code[ConsM[NtrlG[NatD[B]]]]:
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
  G <: ExprG[CollectedUD[NatUD]],
  M <: ConsHM[G],
](terms: Code[M]) extends Code[AddM[G, M]]

/**
  * Outputs the product of the inputs.
  */
final case class Multiply[
  G <: ExprG[CollectedUD[NatUD]],
  M <: ConsHM[G],
](factors: Code[M]) extends Code[MultiplyM[G, M]]

/**
  * Outputs [[True]] iff [[lhs]] is strictly smaller than [[rhs]].
  */
final case class Compare[
  G1 <: ExprG[NatUD], G2 <: ExprG[NatUD],
  M1 <: ConsHM[G1], M2 <: ConsHM[G2],
](lhs: Code[M1], rhs: Code[M2]) extends Code[CompareM[G1, G2, M1, M2]]
