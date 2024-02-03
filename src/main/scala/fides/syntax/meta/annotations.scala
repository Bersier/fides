package fides.syntax.meta

import fides.syntax.code.{Code, CodeType, Expr, Val, ValType}
import fides.syntax.values.NaturalNumber

/**
  * An annotated piece of code. The annotation does not change the semantics of the code.
  * It acts as a structured comment.
  */
final case class Annotated[S <: CodeType, T <: ValType](code: Code[S], annotation: Code[Val[T]]) extends Code[S]

final case class AnnotatedMatcher[S <: CodeType, T <: ValType](
  code: Code[S],
  annotation: Code[Val[T]],
  level: Code[Val[NaturalNumber]] = NaturalNumber(0),
) extends Code[S]

/**
  * Removes all the annotations from a Quoted.
  */
final case class RemoveAnnotations[S <: CodeType](code: Code[Expr[Quoted[S]]]) extends Expr[Quoted[S]]
