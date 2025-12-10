package fides.syntax.meta

import fides.syntax.machinery.*
import util.MultisetOps
import util.Multisets.Multiset

/**
  * Used for multisets of pieces of code, at the syntax level.
  */
transparent sealed trait Args[
  E <: TopE, G <: TopG,
  M <: ConsHM[G],
] extends Code[ArgsM[E, G, M]]:
  def arguments: Multiset[Code[M]]
  override def toString: String = s"Args{${arguments.asInstanceOf[Vector[?]].mkString(", ")}}"
object Args:
  def apply[G <: TopG](): None = NoneImpl
  def apply[
  G <: TopG,
  M <: ConsHM[G],
](first: Code[M], others: Code[M]*): Some[G, M] = new SomeImpl(first, others*)
  def unapply[
    G <: TopG,
    M <: ConsHM[G],
  ](args: Args[?, G, M]): scala.Some[Multiset[Code[M]]] = Some(args.arguments)

  // todo shouldn't it be BotG instead of OffBotG?
  type None = Code[ArgsM[TopE.T, OffBotG, Nothing /*todo BotM*/]]
  type Some[
  G <: TopG,
  M <: ConsHM[G],
] = Code[ArgsM[TopE.F, G, M]]

  final case class Matcher[G <: TopG](
    typeRepr: ConsM[G],
  ) extends ConsM[ArgsG[TopE, G]]
  // todo add another type parameter I?

  private case object NoneImpl extends Args[TopE.T, OffBotG, Nothing], None:
    def arguments: Multiset[Code[Nothing]] = summon[MultisetOps[Multiset]].empty
  private final class SomeImpl[
    G <: TopG,
    M <: ConsHM[G],
  ](first: Code[M], others: Code[M]*) extends Args[TopE.F, G, M], Some[G, M]:
    val arguments: Multiset[Code[M]] = summon[MultisetOps[Multiset]].multiset(elements = first +: others*)
end Args

/**
  * As an Expr, converts a collection of code quotations to a [[Quoted]] of [[Args]] of all the pieces of code.
  *
  * As an Xctr, extracts the arguments out of a [[Quoted]] of [[Args]].
  */
final case class Zip[
  EG <: TopG,
  E <: TopE, EM <: ConsHM[EG], P <: TopP,
  G <: PolarG[CollectedD[E, QuoteD[EM]], P],
  M <: ConsHM[G],
](pieces: Code[M]) extends Code[ZipM[EG, E, EM, P, G, M]]
