package fides.syntax.values

import fides.syntax.types.*
import util.{BoolT, FlseT}

/**
  * General [[PolarS]] for static-size collecting.
  */
final case class Collected[I <: BoolT, P <: TopD, N <: TopD, L <: Boolean](
  elements: OldCode[ArgsS[I, PolarS[P, N, L]]],
) extends OldCode[PolarS[CollectedD[I, P], CollectedD[I, N], L]]

/**
  * As an Expr, outputs a Collected with one element added to it.
  *
  * As an Xctr, (non-deterministically) extracts one element from a Collected.
  */
final case class AddElementP[P <: N, N <: TopD](
  element: OldCode[PolrS[P, N]],
  others: OldCode[PolrS[CollectedUD[P], CollectedUD[N]]],
) extends OldCode[PovrS[CollectedD[FlseT, P], CollectedD[FlseT, N]]]

/**
  * As an Expr, waits for [[size]] elements from [[elementSource]], then outputs them as a Collected.
  *
  * As an Xctr, outputs the elements of a Collected to [[elementSource]], and its size to [[size]].
  */
final case class Collect[P <: TopD, N <: P & TopD](
  elementSource: OldCode[CnstS[ChanD[P, N]]],
  size: OldCode[PolrS[NatUD, NatUD]],
) extends OldCode[PovrS[CollectedUD[P], CollectedUD[N]]]
// todo does it only start collecting after having received [[size]]?
