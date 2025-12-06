package fides.syntax.meta

import fides.syntax.machinery.*
import util.MultisetOps
import util.Multisets.Multiset

/**
  * Used for multisets of pieces of code, at the syntax level.
  */
transparent sealed trait Args[E <: TopE, G <: TopG, Q <: TopQ] extends ConsM[ArgsG[E, G], Q]:
  def arguments: Multiset[ConsM[G, Q]]
  override def toString: String = s"Args{${arguments.asInstanceOf[Vector[?]].mkString(", ")}}"
object Args:
  def apply[G <: TopG](): None = NoneImpl
  def apply[G <: TopG, Q <: TopQ](first: ConsM[G, Q], others: ConsM[G, Q]*): Some[G, Q] = new SomeImpl(first, others*)
  def unapply[G <: TopG, Q <: TopQ](arguments: Args[?, G, Q]): scala.Some[Multiset[ConsM[G, Q]]] =
    Some(arguments.arguments)

  type None = ConsM[ArgsG[TopE.T, OffBotG], BotQ]
  type Some[G <: TopG, Q <: TopQ] = ConsM[ArgsG[TopE.F, G], Q]

  final case class Matcher[G <: TopG, Q <: TopQ](
    typeRepr: ConsM[G, Q],
  ) extends ConsM[ArgsG[TopE, G], Q | ConsQ[GenP[TopB, BotB, BotB], BotQ]]
  // todo add another type parameter I?

  private case object NoneImpl extends Args[TopE.T, OffBotG, BotQ], None:
    def arguments: Multiset[ConsM[OffBotG, BotQ]] = summon[MultisetOps[Multiset]].empty
  private final class SomeImpl[G <: TopG, Q <: TopQ](
    first: ConsM[G, Q],
    others: ConsM[G, Q]*,
  ) extends Args[TopE.F, G, Q], Some[G, Q]:
    val arguments: Multiset[ConsM[G, Q]] = summon[MultisetOps[Multiset]].multiset(elements = first +: others*)
end Args

/**
  * As an Expr, converts a collection of code quotations to a [[Quoted]] of [[Args]] of all the pieces of code.
  *
  * As an Xctr, extracts the arguments out of a [[Quoted]] of [[Args]].
  */
final case class Zip[E <: TopE, G <: TopG, P <: TopP, Q <: TopQ](
  pieces: ConsM[Polar2G[CollectedD[E, QuoteD[G]], P], Q],
) extends ConsM[Polar2G[QuoteD[ArgsG[E, G]], P], Q]
