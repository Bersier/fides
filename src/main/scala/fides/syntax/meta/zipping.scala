package fides.syntax.meta

import fides.syntax.machinery.*
import util.MultisetOps
import util.Multisets.Multiset

/**
  * Used for multisets of pieces of code, at the syntax level.
  */
transparent sealed trait Args[
  E <: TopE, G <: TopG, Q <: TopQ,
  M <: ConsHM[G, Q],
] extends Code[ArgsM[E, G, Q, M]]:
  def arguments: Multiset[Code[M]]
  override def toString: String = s"Args{${arguments.asInstanceOf[Vector[?]].mkString(", ")}}"
object Args:
  def apply[G <: TopG](): None = NoneImpl
  def apply[
  G <: TopG, Q <: TopQ,
  M <: ConsHM[G, Q],
](first: Code[M], others: Code[M]*): Some[G, Q, M] = new SomeImpl(first, others*)
  def unapply[
    G <: TopG, Q <: TopQ,
    M <: ConsHM[G, Q],
  ](args: Args[?, G, Q, M]): scala.Some[Multiset[Code[M]]] = Some(args.arguments)

  // todo shouldn't it be BotG instead of OffBotG?
  type None = Code[ArgsM[TopE.T, OffBotG, BotQ, Nothing /*todo BotM*/]]
  type Some[
  G <: TopG, Q <: TopQ,
  M <: ConsHM[G, Q],
] = Code[ArgsM[TopE.F, G, Q, M]]

  final case class Matcher[G <: TopG, Q <: TopQ](
    typeRepr: ConsM[G, Q],
  ) extends ConsM[ArgsG[TopE, G], Q | ConsQ[GenP[TopB, BotB, BotB], BotQ]]
  // todo add another type parameter I?

  private case object NoneImpl extends Args[TopE.T, OffBotG, BotQ, Nothing], None:
    def arguments: Multiset[ConsM[OffBotG, BotQ]] = summon[MultisetOps[Multiset]].empty
  private final class SomeImpl[
    G <: TopG, Q <: TopQ,
    M <: ConsHM[G, Q],
  ](first: Code[M], others: Code[M]*) extends Args[TopE.F, G, Q, M], Some[G, Q, M]:
    val arguments: Multiset[Code[M]] = summon[MultisetOps[Multiset]].multiset(elements = first +: others*)
end Args

/**
  * As an Expr, converts a collection of code quotations to a [[Quoted]] of [[Args]] of all the pieces of code.
  *
  * As an Xctr, extracts the arguments out of a [[Quoted]] of [[Args]].
  */
final case class Zip[
  EG <: TopG, EQ <: TopQ,
  E <: TopE, EM <: ConsHM[EG, EQ], P <: TopP,
  G <: PolarG[CollectedD[E, QuoteD[EM]], P], Q <: TopQ,
  M <: ConsHM[G, Q],
](pieces: Code[M]) extends Code[ZipM[EG, EQ, E, EM, P, G, Q, M]]
