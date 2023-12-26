package fides2024.syntax

final case class Escape[C <: CodeType](code: Code[Expr[Quotation[C]]]) extends Code[C]
