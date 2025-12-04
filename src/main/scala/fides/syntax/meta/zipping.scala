package fides.syntax.meta

import fides.syntax.types.*
import util.{TopB, MultisetOps}
import util.Multisets.Multiset

/**
  * Used for multisets of pieces of code, at the syntax level.
  */
// todo using TopB to represent inhabitation is wrong here, since Inhabited should not be a subtype of uninhabited
transparent sealed trait Args[S <: TopS, I <: TopB, Q <: TopQ] extends Code[ArgsS[I, S], Q]:
  def arguments: Multiset[Code[S, Q]]
  override def toString: String = s"Args{${arguments.asInstanceOf[Vector[?]].mkString(", ")}}"
object Args:
  def apply[S <: TopS](): None = Empty
  def apply[S <: TopS, Q <: TopQ](first: Code[S, Q], others: Code[S, Q]*): Some[S, Q] = new NonEmpty(first, others*)
  def unapply[S <: TopS, Q <: TopQ](arguments: Args[S, ?, Q]): scala.Some[Multiset[Code[S, Q]]] =
    Some(arguments.arguments)

  type None = Code[ArgsS[TopB.F, OffBotS], BotQ]
  type Some[S <: TopS, Q <: TopQ] = Code[ArgsS[TopB.T, S], Q]

  final case class Matcher[S <: TopS, Q <: TopQ](
    typeRepr: Code[S, Q],
  ) extends Code[ArgsS[TopB, S], Q | SomeQ[Polarity[TopB.F, TopB.T, TopB.T], BotQ]]
  // todo add another type parameter I?

  private case object Empty extends Args[OffBotS, TopB.F, BotQ], None:
    def arguments: Multiset[Code[OffBotS, BotQ]] = summon[MultisetOps[Multiset]].empty
  private final class NonEmpty[S <: TopS, Q <: TopQ](
    first: Code[S, Q],
    others: Code[S, Q]*,
  ) extends Args[S, TopB.T, Q], Some[S, Q]:
    val arguments: Multiset[Code[S, Q]] = summon[MultisetOps[Multiset]].multiset(elements = first +: others*)
end Args

/**
  * As an Expr, converts a collection of code quotations to a [[Quoted]] of [[Args]] of all the pieces of code.
  *
  * As an Xctr, extracts the arguments out of a [[Quoted]] of [[Args]].
  */
final case class Zip[IsNonEmpty <: TopB, S <: TopS, P <: TopP, Q <: TopQ](
  pieces: Code[Polar2[CollectedT[IsNonEmpty, QuoteT[S]], P], Q],
) extends Code[Polar2[QuoteT[ArgsS[IsNonEmpty, S]], P], Q]
