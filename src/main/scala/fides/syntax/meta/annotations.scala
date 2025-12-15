package fides.syntax.meta

import fides.syntax.machinery.*
import typelevelnumbers.binary.Bits

/**
  * An annotated piece of code. The annotation does not change the semantics of the code.
  * It acts as a structured comment.
  */
final case class Annotated[G <: TopG, D <: TopD](
  code: GenM[G],
  annotation: GenM[CnstG[D]],
) extends GenM[G]

final case class AnnotatedMatcher[G <: TopG, B <: Bits, D <: TopD](
  code: GenM[G],
  annotation: GenM[CnstG[D]],
  level: GenM[NtrlG[NatD[B]]]// = NatLit(Bits.None),
) extends GenM[G]
// todo... delete?
