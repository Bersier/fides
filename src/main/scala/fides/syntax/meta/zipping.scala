package fides.syntax.meta

import fides.syntax.types.*
import util.{Bool, MultisetOps}
import util.Multisets.Multiset

/**
  * Used for multisets of pieces of code, at the syntax level.
  */
sealed trait Args[S <: TopS, I <: Bool, M <: TopM] extends Code[ArgsS[I, S], M]:
  def arguments: Multiset[Code[S, M]]
  override def toString: String = s"Args{${arguments.asInstanceOf[Vector[?]].mkString(", ")}}"
object Args:
  def apply[S <: TopS](): None = Empty
  def apply[S <: TopS, M <: TopM](first: Code[S, M], others: Code[S, M]*): Some[S, M] = new NonEmpty(first, others*)
  def unapply[S <: TopS, M <: TopM](arguments: Args[S, ?, M]): scala.Some[Multiset[Code[S, M]]] =
    Some(arguments.arguments)

  type None = Code[ArgsS[Bool.F, OffBotS], BotM]
  type Some[S <: TopS, M <: TopM] = Code[ArgsS[Bool.T, S], M]

  private case object Empty extends Args[OffBotS, Bool.F, BotM], None:
    def arguments: Multiset[Code[OffBotS, BotM]] = summon[MultisetOps[Multiset]].empty
  private final class NonEmpty[S <: TopS, M <: TopM](
    first: Code[S, M],
    others: Code[S, M]*,
  ) extends Args[S, Bool.T, M], Some[S, M]:
    val arguments: Multiset[Code[S, M]] = summon[MultisetOps[Multiset]].multiset(elements = first +: others*)
end Args

/**
  * As an Expr, converts a collection of code quotations to a [[Quoted]] of [[Args]] of all the pieces of code.
  *
  * As an Xctr, extracts the arguments out of a [[Quoted]] of [[Args]].
  */
final case class Zip[IsNonEmpty <: Bool, S <: TopS, P <: TopP, M <: TopM](
  pieces: Code[Polar2[CollectedT[IsNonEmpty, QuotedT[S]], P], M],
) extends Code[Polar2[QuotedT[ArgsS[IsNonEmpty, S]], P], M]
