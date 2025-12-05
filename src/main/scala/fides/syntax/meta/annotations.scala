package fides.syntax.meta

import fides.syntax.types.*
import typelevelnumbers.binary.Bits

/**
  * An annotated piece of code. The annotation does not change the semantics of the code.
  * It acts as a structured comment.
  */
final case class Annotated[S <: TopS, D <: TopD, Q <: TopQ](
  code: ConsM[S, Q],
  annotation: ConsM[Cnst2S[D], Q],
) extends ConsM[S, Q]

final case class AnnotatedMatcher[S <: TopS, B <: Bits, D <: TopD, Q <: TopQ](
  code: ConsM[S, Q],
  annotation: ConsM[Cnst2S[D], Q],
  level: ConsM[Ntrl2S[NatD[B]], Q]// = NatLit(Bits.None),
) extends ConsM[S, Q]
// todo... delete?
