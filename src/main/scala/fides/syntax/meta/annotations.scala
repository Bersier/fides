package fides.syntax.meta

import fides.syntax.types.*
import fides.syntax.values.Nat
import typelevelnumbers.binary.Bits

/**
  * An annotated piece of code. The annotation does not change the semantics of the code.
  * It acts as a structured comment.
  */
final case class Annotated[S <: TopS, T <: TopT, M <: TopM](
  code: Code2[S, M],
  annotation: Code2[Cnst2[T], M],
) extends Code2[S, M]

final case class AnnotatedMatcher[S <: TopS, B <: Bits, T <: TopT, M <: TopM](
  code: Code2[S, M],
  annotation: Code2[Cnst2[T], M],
  level: Code2[Ntrl2[NatT[B]], M] = Nat(Bits.None),
) extends Code2[S, M]
