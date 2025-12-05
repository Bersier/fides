package fides.syntax.meta

import fides.syntax.types.*
import util.Multisets.Multiset
import util.{BotB, MultisetOps, TopB}

/**
  * Used for multisets of pieces of code, at the syntax level.
  */
// todo using TopB to represent inhabitation is wrong here, since Inhabited should not be a subtype of uninhabited
transparent sealed trait Args[S <: TopS, I <: TopB, Q <: TopQ] extends ConsM[ArgsS[I, S], Q]:
  def arguments: Multiset[ConsM[S, Q]]
  override def toString: String = s"Args{${arguments.asInstanceOf[Vector[?]].mkString(", ")}}"
object Args:
  def apply[S <: TopS](): None = Empty
  def apply[S <: TopS, Q <: TopQ](first: ConsM[S, Q], others: ConsM[S, Q]*): Some[S, Q] = new NonEmpty(first, others*)
  def unapply[S <: TopS, Q <: TopQ](arguments: Args[S, ?, Q]): scala.Some[Multiset[ConsM[S, Q]]] =
    Some(arguments.arguments)

  type None = ConsM[ArgsS[TopB, OffBotS], BotQ]
  type Some[S <: TopS, Q <: TopQ] = ConsM[ArgsS[BotB, S], Q]

  final case class Matcher[S <: TopS, Q <: TopQ](
    typeRepr: ConsM[S, Q],
  ) extends ConsM[ArgsS[TopB, S], Q | ConsQ[Polarity[TopB, BotB, BotB], BotQ]]
  // todo add another type parameter I?

  private case object Empty extends Args[OffBotS, TopB, BotQ], None:
    def arguments: Multiset[ConsM[OffBotS, BotQ]] = summon[MultisetOps[Multiset]].empty
  private final class NonEmpty[S <: TopS, Q <: TopQ](
    first: ConsM[S, Q],
    others: ConsM[S, Q]*,
  ) extends Args[S, BotB, Q], Some[S, Q]:
    val arguments: Multiset[ConsM[S, Q]] = summon[MultisetOps[Multiset]].multiset(elements = first +: others*)
end Args

/**
  * As an Expr, converts a collection of code quotations to a [[Quoted]] of [[Args]] of all the pieces of code.
  *
  * As an Xctr, extracts the arguments out of a [[Quoted]] of [[Args]].
  */
final case class Zip[IsNonEmpty <: TopB, S <: TopS, P <: TopP, Q <: TopQ](
  pieces: ConsM[Polar2S[CollectedD[IsNonEmpty, QuoteD[S]], P], Q],
) extends ConsM[Polar2S[QuoteD[ArgsS[IsNonEmpty, S]], P], Q]
