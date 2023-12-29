package fides2024.syntax

final case class Escape[P <: [U <: ValType] =>> Polar[U], C <: CodeType](code: Code[P[Quotation[C]]]) extends Code[C]
// todo should we keep track of the polarity in the extended type?
