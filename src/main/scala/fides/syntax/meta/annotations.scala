package fides.syntax.meta

import fides.syntax.types.*
import fides.syntax.values.Nat
import typelevelnumbers.binary.Bits

/**
  * An annotated piece of code. The annotation does not change the semantics of the code.
  * It acts as a structured comment.
  */
final case class Annotated[S <: TopS, T <: TopT, M <: TopM](
  code: Code[S, M],
  annotation: Code[Cnst2[T], M],
) extends Code[S, M]

final case class AnnotatedMatcher[S <: TopS, B <: Bits, T <: TopT, M <: TopM](
  code: Code[S, M],
  annotation: Code[Cnst2[T], M],
  level: Code[Ntrl2[NatT[B]], M] = Nat(Bits.None),
) extends Code[S, M]
