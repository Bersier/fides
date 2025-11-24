package fides.syntax.values

import fides.syntax.core.Code
import fides.syntax.types.{ArgsS, BotT, ChanT, Cnst, CollectedT, NaturalNumberT, Ntrl, Polr, Povr, TopT}

/**
  * A literal for a value that is made up of an unordered collection of values.
  */
final case class Collected[IsNonEmpty <: Boolean, T >: BotT <: TopT](
  elements: Code[ArgsS[IsNonEmpty, Cnst[T]]],
) extends Code[Ntrl[CollectedT[IsNonEmpty, T]]]

/**
  * As an Expr, outputs a Collected with one element added to it.
  *
  * As an Xctr, (non-deterministically) extracts one element from a Collected.
  */
final case class AddElementP[P <: N, N <: TopT](
  element: Code[Polr[P, N]],
  others: Code[Polr[CollectedT[Boolean, P], CollectedT[Boolean, N]]],
) extends Code[Povr[CollectedT[true, P], CollectedT[true, N]]]

/**
  * As an Expr, waits for [[size]] elements from [[elementSource]], then outputs them as a Collected.
  *
  * As an Xctr, outputs the elements of a Collected to [[elementSource]], and its size to [[size]].
  */
final case class Collect[P >: BotT, N <: P & TopT](
  elementSource: Code[Cnst[ChanT[P, N]]], // todo replace by Loc?
  size: Code[Polr[NaturalNumberT, NaturalNumberT]],
) extends Code[Povr[CollectedT[Boolean, P], CollectedT[Boolean, N]]]
// todo does it only start collecting after having received [[size]]?
