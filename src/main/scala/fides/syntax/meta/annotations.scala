package fides.syntax.meta

import fides.syntax.types.*
import fides.syntax.values.Nat
import typelevelnumbers.binary.Bits

/**
  * An annotated piece of code. The annotation does not change the semantics of the code.
  * It acts as a structured comment.
  */
final case class Annotated[S <: TopS, D <: TopD, Q <: TopQ](
  code: ConsC[S, Q],
  annotation: ConsC[Cnst2S[D], Q],
) extends ConsC[S, Q]

final case class AnnotatedMatcher[S <: TopS, B <: Bits, D <: TopD, Q <: TopQ](
  code: ConsC[S, Q],
  annotation: ConsC[Cnst2S[D], Q],
  level: ConsC[Ntrl2S[NatD[B]], Q] = Nat(Bits.None),
) extends ConsC[S, Q]
