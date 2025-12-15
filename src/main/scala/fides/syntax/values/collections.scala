package fides.syntax.values

import fides.syntax.machinery.*
import typelevelnumbers.binary.Bits

/**
  * General polar for static-size collecting.
  */
final case class Collected[
  D <: TopD, P <: TopP,
  E <: TopE, EG <: PolarG[D, P],
  G <: ArgsG[E, EG],
  M <: GenHM[G],
](elements: Code[M]) extends Code[CollectedM[D, P, E, EG, G, M]]

/**
  * As an [[ExprG]], outputs a Collected with one element added to it.
  *
  * As an [[XctrG]], (non-deterministically) extracts one element from a Collected.
  */
final case class AddElement[
  D <: TopD, EP <: TopP, P <: TopP,
  EG <: PolarG[D, EP], G <: PolarG[CollectedUD[D], P],
  EM <: GenHM[EG], M <: GenHM[G],
](element: Code[EM], others: Code[M]) extends Code[AddElementM[D, EP, P, EG, G, EM, M]]

/**
  * As an [[ExprG]], waits for [[size]] elements from [[elementSource]], then outputs them as a Collected.
  *
  * As an [[XctrG]], outputs the elements of a Collected to [[elementSource]], and its size to [[size]].
  */
final case class Collect[
  K <: TopK, D <: TopD, P >: BotVP <: TopP, B <: Bits,
  SG <: ChanRefG[K, D, P], NG <: NtrlG[NatD[B]],
  SM <: GenHM[SG], NM <: GenHM[NG],
](elementSource: Code[SM], size: Code[NM]) extends Code[CollectM[K, D, P, B, SG, NG, SM, NM]]
// todo does it only start collecting after having received [[size]]?
