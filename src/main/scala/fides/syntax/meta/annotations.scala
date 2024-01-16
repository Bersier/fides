package fides.syntax.meta

import fides.syntax.code.{Code, CodeType, Expr, Val, ValType}

/**
  * An annotated piece of code. The annotation does not change the semantics of the code.
  * It acts as a structured comment.
  */
final case class Annotated[C <: CodeType, T <: ValType]
(code: Code[C], annotation: Code[Val[T]]) extends Code[C], CodeType

/**
  * Removes all the annotations from a Quoted.
  */
final case class RemoveAnnotations[C <: CodeType](code: Code[Expr[Quoted[C]]]) extends Expr[Quoted[C]]
