package core.syntax

trait R[+K <: I] extends Lex[K]

final case class Duplicate(out1: Out[_, _], out2: Out[_, _]) extends R[_]