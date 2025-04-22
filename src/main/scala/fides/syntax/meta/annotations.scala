package fides.syntax.meta

import fides.syntax.code.{Code, CodeType, Expr, Lit, ValTop}
import fides.syntax.values.NaturalNumber

/**
  * An annotated piece of code. The annotation does not change the semantics of the code.
  * It acts as a structured comment.
  */
final case class Annotated[S <: CodeType, T <: ValTop](code: Code[S], annotation: Code[Lit[T]]) extends Code[S]

final case class AnnotatedMatcher[S <: CodeType, T <: ValTop](
  code: Code[S],
  annotation: Code[Lit[T]],
  level: Code[Lit[NaturalNumber]] = NaturalNumber(0),
) extends Code[S]

/**
  * Removes all the annotations from a Quoted.
  */
final case class RemoveAnnotations[S <: CodeType](code: Code[Expr[Quoted[S]]]) extends Expr[Quoted[S]]
