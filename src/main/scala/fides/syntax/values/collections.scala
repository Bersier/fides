package fides.syntax.values

import fides.syntax.machinery.*
import typelevelnumbers.binary.Bits

/**
  * General [[OldPolarG]] for static-size collecting.
  */
final case class Collected[
  D <: TopD, P <: TopP,
  E <: TopE, EG <: PolarG[D, P],
  G <: ArgsG[E, EG], Q <: TopQ,
  M <: ConsM[G, Q],
](elements: Code[M]) extends Code[CollectedM[D, P, E, EG, G, Q, M]]

/**
  * As an [[ExprG]], outputs a Collected with one element added to it.
  *
  * As an [[XctrG]], (non-deterministically) extracts one element from a Collected.
  */
final case class AddElement[
  D <: TopD, P <: TopP,
  EG <: PolarG[D, P], G <: PolarG[CollectedUD[D], P], Q <: TopQ,
  EM <: ConsM[EG, Q], M <: ConsM[G, Q],
](element: Code[EM], others: Code[M]) extends Code[AddElementM[D, P, EG, G, Q, EM, M]]

/**
  * As an [[ExprG]], waits for [[size]] elements from [[elementSource]], then outputs them as a Collected.
  *
  * As an [[XctrG]], outputs the elements of a Collected to [[elementSource]], and its size to [[size]].
  */
final case class Collect[
  D <: TopD, P <: TopP, B <: Bits,
  SG <: NtrlG[ChanD[?, ?]], NG <: NtrlG[NatD[B]], Q <: TopQ, // todo
  SM <: ConsM[SG, Q], NM <: ConsM[NG, Q],
](elementSource: Code[SM], size: Code[NM]) extends Code[CollectM[D, P, B, SG, NG, Q, SM, NM]]
// todo does it only start collecting after having received [[size]]?
