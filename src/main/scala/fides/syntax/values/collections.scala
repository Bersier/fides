package fides.syntax.values

import fides.syntax.machinery.*
import typelevelnumbers.binary.Bits

/**
  * General [[PolarG]] for static-size collecting.
  */
final case class Collected[
  D <: TopD, P <: TopP,
  E <: TopE, EG <: Polar2G[D, P],
  G <: ArgsG[E, EG], Q <: TopQ,
  M <: ConsM[G, Q],
](elements: Code[M]) extends Code[CollectedM[D, P, E, EG, G, Q, M]]

/**
  * As an [[Expr2G]], outputs a Collected with one element added to it.
  *
  * As an [[Xctr2G]], (non-deterministically) extracts one element from a Collected.
  */
final case class AddElement[
  D <: TopD, P <: TopP,
  EG <: Polar2G[D, P], G <: Polar2G[CollectedUD[D], P], Q <: TopQ,
  EM <: ConsM[EG, Q], M <: ConsM[G, Q],
](element: Code[EM], others: Code[M]) extends Code[AddElementM[D, P, EG, G, Q, EM, M]]

/**
  * As an [[Expr2G]], waits for [[size]] elements from [[elementSource]], then outputs them as a Collected.
  *
  * As an [[Xctr2G]], outputs the elements of a Collected to [[elementSource]], and its size to [[size]].
  */
final case class Collect[
  D <: TopD, P <: TopP, B <: Bits,
  SG <: Ntrl2G[ChanD[?, ?]], NG <: Ntrl2G[NatD[B]], Q <: TopQ, // todo
  SM <: ConsM[SG, Q], NM <: ConsM[NG, Q],
](elementSource: Code[SM], size: Code[NM]) extends Code[CollectM[D, P, B, SG, NG, Q, SM, NM]]
// todo does it only start collecting after having received [[size]]?
