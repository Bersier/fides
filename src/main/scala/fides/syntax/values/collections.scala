package fides.syntax.values

import fides.syntax.types.*

/**
  * General [[PolarG]] for static-size collecting.
  */
final case class Collected[I <: Empty, P <: TopD, N <: TopD, L <: Boolean](
  elements: OldCode[ArgsG[I, PolarG[P, N, L]]],
) extends OldCode[PolarG[CollectedD[I, P], CollectedD[I, N], L]]
// todo should we have a typeful version of this?

/**
  * As an Expr, outputs a Collected with one element added to it.
  *
  * As an Xctr, (non-deterministically) extracts one element from a Collected.
  */
final case class AddElementP[P <: N, N <: TopD](
  element: OldCode[PolrG[P, N]],
  others: OldCode[PolrG[CollectedUD[P], CollectedUD[N]]],
) extends OldCode[PovrG[CollectedD[Empty.T, P], CollectedD[Empty.T, N]]]

/**
  * As an Expr, waits for [[size]] elements from [[elementSource]], then outputs them as a Collected.
  *
  * As an Xctr, outputs the elements of a Collected to [[elementSource]], and its size to [[size]].
  */
final case class Collect[P <: TopD, N <: P & TopD](
  elementSource: OldCode[CnstG[ChanD[P, N]]],
  size: OldCode[PolrG[NatUD, NatUD]],
) extends OldCode[PovrG[CollectedUD[P], CollectedUD[N]]]
// todo does it only start collecting after having received [[size]]?
