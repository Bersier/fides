package fides.syntax.values

import fides.syntax.core.Code
import fides.syntax.types.{ArgsS, BotT, ChanT, Cnst, CollectedT, CollectedUT, NatUT, Polar, Polr, Povr, TopT}

/**
  * General [[Polar]] for static-size collecting.
  */
final case class Collected[IsNonEmpty <: Boolean, P >: BotT, N <: TopT, L <: Boolean](
  elements: Code[ArgsS[IsNonEmpty, Polar[P, N, L]]],
) extends Code[Polar[CollectedT[IsNonEmpty, P], CollectedT[IsNonEmpty, N], L]]

/**
  * As an Expr, outputs a Collected with one element added to it.
  *
  * As an Xctr, (non-deterministically) extracts one element from a Collected.
  */
final case class AddElementP[P <: N, N <: TopT](
  element: Code[Polr[P, N]],
  others: Code[Polr[CollectedUT[P], CollectedUT[N]]],
) extends Code[Povr[CollectedT[true, P], CollectedT[true, N]]]

/**
  * As an Expr, waits for [[size]] elements from [[elementSource]], then outputs them as a Collected.
  *
  * As an Xctr, outputs the elements of a Collected to [[elementSource]], and its size to [[size]].
  */
final case class Collect[P >: BotT, N <: P & TopT](
  elementSource: Code[Cnst[ChanT[P, N]]],
  size: Code[Polr[NatUT, NatUT]],
) extends Code[Povr[CollectedUT[P], CollectedUT[N]]]
// todo does it only start collecting after having received [[size]]?
