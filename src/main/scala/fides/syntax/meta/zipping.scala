package fides.syntax.meta

import fides.syntax.types.{ArgsS, Code, CollectedT, OffBotS, Polr, Povr, QuotedT, TopS}
import util.MultisetOps
import util.Multisets.Multiset

/**
  * Used for multisets of pieces of code, at the syntax level.
  */
sealed trait Args[+S <: TopS] extends Code[ArgsS[Boolean, S]]:
  def arguments: Multiset[Code[S]]
  override def toString: String = s"Args{${arguments.asInstanceOf[Vector[?]].mkString(", ")}}"
object Args:
  def apply[S <: TopS](): None = Empty
  def apply[S <: TopS](first: Code[S], others: Code[S]*): Some[S] = new NonEmpty(first, others*)
  def unapply[S <: TopS](arguments: Args[S]): scala.Some[Multiset[Code[S]]] =
    Some(arguments.arguments)

  type None = Code[ArgsS[false, OffBotS]]
  type Some[+S <: TopS] = Code[ArgsS[true, S]]

  private case object Empty extends Args[OffBotS], None:
    def arguments: Multiset[Code[OffBotS]] = summon[MultisetOps[Multiset]].empty
  private final class NonEmpty[+S <: TopS](first: Code[S], others: Code[S]*) extends Args[S], Some[S]:
    val arguments: Multiset[Code[S]] = summon[MultisetOps[Multiset]].multiset(elements = first +: others*)
end Args

/**
  * As an Expr, converts a collection of code quotations to a [[Quoted]] of [[Args]] of all the pieces of code.
  *
  * As an Xctr, extracts the arguments out of a [[Quoted]] of [[Args]].
  */
final case class Zip[
  IsNonEmptyP <: Boolean,
  IsNonEmptyN <: Boolean,
  P <: TopS,
  N <: TopS,
](
  pieces: Code[Polr[CollectedT[IsNonEmptyP, QuotedT[P]], CollectedT[IsNonEmptyN, QuotedT[N]]]],
) extends Code[Povr[QuotedT[ArgsS[IsNonEmptyP, P]], QuotedT[ArgsS[IsNonEmptyN, N]]]]
