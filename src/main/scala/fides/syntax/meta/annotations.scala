package fides.syntax.meta

import fides.syntax.types.*
import fides.syntax.values.Nat
import typelevelnumbers.binary.Bits

/**
  * An annotated piece of code. The annotation does not change the semantics of the code.
  * It acts as a structured comment.
  */
final case class Annotated[S <: TopS, T <: TopT, Q <: TopQ](
  code: Code[S, Q],
  annotation: Code[Cnst2[T], Q],
) extends Code[S, Q]

final case class AnnotatedMatcher[S <: TopS, B <: Bits, T <: TopT, Q <: TopQ](
  code: Code[S, Q],
  annotation: Code[Cnst2[T], Q],
  level: Code[Ntrl2[NatT[B]], Q] = Nat(Bits.None),
) extends Code[S, Q]
