package fides2024.syntax.code

final case class VarArgs[+C <: CodeType](pieces: Code[C]*) extends Code[VarArgs[C]], CodeType

final case class Annotated[C <: CodeType, T <: ValType]
(code: Code[C], annotation: Code[Val[T]]) extends Code[C], CodeType
// todo add primitive to remove annotations?

// todo add way to define custom Expr, Xctr, ...? As some light syntactic sugar?
