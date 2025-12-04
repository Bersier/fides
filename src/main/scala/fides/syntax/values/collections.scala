package fides.syntax.values

import fides.syntax.types.*
import util.Bool

/**
  * General [[Polar]] for static-size collecting.
  */
final case class Collected[IsNonEmpty <: Bool, P >: BotT, N <: TopT, L <: Boolean](
  elements: OldCode[ArgsS[IsNonEmpty, Polar[P, N, L]]],
) extends OldCode[Polar[CollectedT[IsNonEmpty, P], CollectedT[IsNonEmpty, N], L]]

/**
  * As an Expr, outputs a Collected with one element added to it.
  *
  * As an Xctr, (non-deterministically) extracts one element from a Collected.
  */
final case class AddElementP[P <: N, N <: TopT](
  element: OldCode[Polr[P, N]],
  others: OldCode[Polr[CollectedUT[P], CollectedUT[N]]],
) extends OldCode[Povr[CollectedT[Bool.T, P], CollectedT[Bool.T, N]]]

/**
  * As an Expr, waits for [[size]] elements from [[elementSource]], then outputs them as a Collected.
  *
  * As an Xctr, outputs the elements of a Collected to [[elementSource]], and its size to [[size]].
  */
final case class Collect[P >: BotT, N <: P & TopT](
  elementSource: OldCode[Cnst[ChanT[P, N]]],
  size: OldCode[Polr[NatUT, NatUT]],
) extends OldCode[Povr[CollectedUT[P], CollectedUT[N]]]
// todo does it only start collecting after having received [[size]]?
