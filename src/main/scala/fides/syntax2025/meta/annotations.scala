package fides.syntax2025.meta

import fides.syntax2025.machinery.*
import typelevelnumbers.binary.Bits

/**
  * An annotated piece of code. The annotation does not change the semantics of the code.
  * It acts as a structured comment.
  *
  * If the annotation is bound to a quote, then it belongs to that quote.
  */
final case class Annotated[K <: TopK, G <: TopG, D <: TopD](
  quoteName: GenM[NameG[K]],
  code: GenM[G],
  annotation: GenM[CnstG[D]],
) extends GenM[G]
