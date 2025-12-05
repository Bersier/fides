package fides.syntax.meta

import fides.syntax.types.*
import typelevelnumbers.binary.Bits

/**
  * An annotated piece of code. The annotation does not change the semantics of the code.
  * It acts as a structured comment.
  */
final case class Annotated[G <: TopG, D <: TopD, Q <: TopQ](
  code: ConsM[G, Q],
  annotation: ConsM[Cnst2G[D], Q],
) extends ConsM[G, Q]

final case class AnnotatedMatcher[G <: TopG, B <: Bits, D <: TopD, Q <: TopQ](
  code: ConsM[G, Q],
  annotation: ConsM[Cnst2G[D], Q],
  level: ConsM[Ntrl2G[NatD[B]], Q]// = NatLit(Bits.None),
) extends ConsM[G, Q]
// todo... delete?
