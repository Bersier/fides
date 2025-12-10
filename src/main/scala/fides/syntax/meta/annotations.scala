package fides.syntax.meta

import fides.syntax.machinery.*
import typelevelnumbers.binary.Bits

/**
  * An annotated piece of code. The annotation does not change the semantics of the code.
  * It acts as a structured comment.
  */
final case class Annotated[G <: TopG, D <: TopD, Q <: TopQ](
  code: ConsM[G],
  annotation: ConsM[CnstG[D]],
) extends ConsM[G]

final case class AnnotatedMatcher[G <: TopG, B <: Bits, D <: TopD, Q <: TopQ](
  code: ConsM[G],
  annotation: ConsM[CnstG[D]],
  level: ConsM[NtrlG[NatD[B]]]// = NatLit(Bits.None),
) extends ConsM[G]
// todo... delete?
