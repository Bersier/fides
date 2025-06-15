package fides.syntax.meta

import fides.syntax.core.Code
import fides.syntax.types.{Cnst, CodeType, Expr, NaturalNumberT, QuotedT, TopT}
import fides.syntax.values.NaturalNumber

/**
  * An annotated piece of code. The annotation does not change the semantics of the code.
  * It acts as a structured comment.
  */
final case class Annotated[S <: CodeType, T <: TopT](code: Code[S], annotation: Code[Cnst[T]]) extends Code[S]

final case class AnnotatedMatcher[S <: CodeType, T <: TopT](
  code: Code[S],
  annotation: Code[Cnst[T]],
  level: Code[Cnst[NaturalNumberT]] = NaturalNumber(0),
) extends Code[S]

/**
  * Removes all the annotations from a Quoted.
  */
final case class RemoveAnnotations[S <: CodeType](code: Code[Expr[QuotedT[S]]]) extends Code[Expr[QuotedT[S]]]
