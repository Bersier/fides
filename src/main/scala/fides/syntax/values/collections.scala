package fides.syntax.values

import fides.syntax.core.Code
import fides.syntax.types.{BotT, ChanT, Cnst, BagT, CollectedT, NaturalNumberT, Ntrl, OffBotT, Polr, Povr, TopT}

/**
  * A literal for a value that is made up of an unordered collection of values.
  */
object Collected: // todo
  def apply[T <: TopT](): None = Empty
  def apply[T <: TopT](first: Code[Cnst[T]], others: Code[Cnst[T]]*): Some[T] =
    new NonEmpty(first, others*)

  type None = Code[Ntrl[CollectedT[false, OffBotT]]]
  type Some[T <: TopT] = Code[Ntrl[CollectedT[true, T]]]

  private case object Empty extends None:
    def elements: Iterable[Code[Cnst[OffBotT]]] = Iterable.empty
  private final class NonEmpty[T <: TopT](first: Code[Cnst[T]], others: Code[Cnst[T]]*) extends Some[T]:
    val elements: Iterable[Code[Cnst[T]]] = first +: others
end Collected
// todo Constructor should use Args

/**
  * As an Expr, outputs a Collected with one element added to it.
  *
  * As an Xctr, (non-deterministically) extracts one element from a Collected.
  *
  * [[AddElement]]`[T] <: `[[Expr]]`[`[[Collected.Some]]`[T]]`
  */
final case class AddElementP[P <: N, N <: TopT](
  element: Code[Polr[P, N]],
  others: Code[Polr[BagT[P], BagT[N]]],
) extends Code[Povr[CollectedT[true, P], CollectedT[true, N]]]

/**
  * As an Expr, waits for [[size]] elements from [[elementSource]], then outputs them as a Collected.
  *
  * As an Xctr, outputs the elements of a Collected to [[elementSource]], and its size to [[size]].
  */
final case class Collect[P >: BotT, N <: P & TopT](
  elementSource: Code[Cnst[ChanT[P, N]]], // todo replace by Loc?
  size: Code[Polr[NaturalNumberT, NaturalNumberT]],
) extends Code[Povr[BagT[P], BagT[N]]]
// todo does it only start collecting after having received [[size]]?
