package fides.syntax.meta

import fides.syntax.machinery.*
import typelevelnumbers.binary.Bits

/**
  * An annotated piece of code. The annotation does not change the semantics of the code.
  * It acts as a structured comment.
  */
final case class Annotated[G <: TopG, D <: TopD, Q <: TopQ](
  code: ConsM[G, Q],
  annotation: ConsM[CnstG[D], Q],
) extends ConsM[G, Q]

final case class AnnotatedMatcher[G <: TopG, B <: Bits, D <: TopD, Q <: TopQ](
  code: ConsM[G, Q],
  annotation: ConsM[CnstG[D], Q],
  level: ConsM[NtrlG[NatD[B]], Q]// = NatLit(Bits.None),
) extends ConsM[G, Q]
// todo... delete?
