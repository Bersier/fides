package fides.syntax.meta

import fides.syntax.types.*
import util.Multisets.Multiset
import util.MultisetOps

/**
  * Used for multisets of pieces of code, at the syntax level.
  */
transparent sealed trait Args[E <: Empty, S <: TopS, Q <: TopQ] extends ConsM[ArgsS[E, S], Q]:
  def arguments: Multiset[ConsM[S, Q]]
  override def toString: String = s"Args{${arguments.asInstanceOf[Vector[?]].mkString(", ")}}"
object Args:
  def apply[S <: TopS](): None = NoneImpl
  def apply[S <: TopS, Q <: TopQ](first: ConsM[S, Q], others: ConsM[S, Q]*): Some[S, Q] = new SomeImpl(first, others*)
  def unapply[S <: TopS, Q <: TopQ](arguments: Args[?, S, Q]): scala.Some[Multiset[ConsM[S, Q]]] =
    Some(arguments.arguments)

  type None = ConsM[ArgsS[Empty.T, OffBotS], BotQ]
  type Some[S <: TopS, Q <: TopQ] = ConsM[ArgsS[Empty.F, S], Q]

  final case class Matcher[S <: TopS, Q <: TopQ](
    typeRepr: ConsM[S, Q],
  ) extends ConsM[ArgsS[Empty, S], Q | ConsQ[Polarity[TopB, BotB, BotB], BotQ]]
  // todo add another type parameter I?

  private case object NoneImpl extends Args[Empty.T, OffBotS, BotQ], None:
    def arguments: Multiset[ConsM[OffBotS, BotQ]] = summon[MultisetOps[Multiset]].empty
  private final class SomeImpl[S <: TopS, Q <: TopQ](
    first: ConsM[S, Q],
    others: ConsM[S, Q]*,
  ) extends Args[Empty.F, S, Q], Some[S, Q]:
    val arguments: Multiset[ConsM[S, Q]] = summon[MultisetOps[Multiset]].multiset(elements = first +: others*)
end Args

/**
  * As an Expr, converts a collection of code quotations to a [[Quoted]] of [[Args]] of all the pieces of code.
  *
  * As an Xctr, extracts the arguments out of a [[Quoted]] of [[Args]].
  */
final case class Zip[E <: Empty, S <: TopS, P <: TopP, Q <: TopQ](
  pieces: ConsM[Polar2S[CollectedD[E, QuoteD[S]], P], Q],
) extends ConsM[Polar2S[QuoteD[ArgsS[E, S]], P], Q]
