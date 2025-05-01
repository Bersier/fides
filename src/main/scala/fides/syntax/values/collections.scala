package fides.syntax.values

import fides.syntax.core.Code
import fides.syntax.types.{ChanT, Collected, CollectedT, Expr, Lit, NaturalNumberT, Ntrl, OffBot, Polar, ValBot, ValTop}

/**
  * A literal for a value that is made up of an unordered collection of values.
  */
object Collected:
  def apply[T <: ValTop](): None = Empty
  def apply[T <: ValTop](first: Code[Lit & Ntrl[T]], others: Code[Lit & Ntrl[T]]*): Some[T] =
    new NonEmpty(first, others*)

  type None = Code[Lit & Ntrl[CollectedT[false, OffBot]]]
  type Some[T <: ValTop] = Code[Lit & Ntrl[CollectedT[true, T]]]

  private case object Empty extends None:
    def elements: Iterable[Code[Lit & Ntrl[OffBot]]] = Iterable.empty
  private final class NonEmpty[T <: ValTop](first: Code[Lit & Ntrl[T]], others: Code[Lit & Ntrl[T]]*) extends Some[T]:
    val elements: Iterable[Code[Lit & Ntrl[T]]] = first +: others
end Collected

/**
  * As an Expr, outputs a Collected with one element added to it.
  *
  * As an Xctr, (non-deterministically) extracts one element from a Collected.
  *
  * [[AddElement]]`[T] <: `[[Expr]]`[`[[Collected.Some]]`[T]]`
  */
final case class AddElementP[P <: N, N <: ValTop](
  element: Code[Polar[P, N]],
  others: Code[Polar[Collected[P], Collected[N]]],
) extends Code[Polar[CollectedT[true, P], CollectedT[true, N]]]

/**
  * As an Expr, waits for [[size]] elements from [[elementSource]], then outputs them as a Collected.
  *
  * As an Xctr, outputs the elements of a Collected to [[elementSource]], and its size to [[size]].
  */
final case class Collect[P >: ValBot, N <: ValTop](
  elementSource: Code[Lit & Expr[ChanT[P, N]]], // TODO replace by Loc?
  size: Code[Polar[NaturalNumberT, NaturalNumberT]],
) extends Code[Polar[Collected[P], Collected[N]]]
