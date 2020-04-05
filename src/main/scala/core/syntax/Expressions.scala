package core.syntax

sealed trait Expr[+K <: I, +T <: V[K]] extends Inp[K, T] // maybe the relation should be the other way around?

final case class Constant[K <: I, T <: V[K]](value: T) extends Expr[K, T]
// This should take some kind of expression, instead of a value... the opposite of a pattern (that outputs stuff)...
// ... an expression that waits for inputs

final case class Join[K <: I, S <: V[K], T <: V[K]](one: Inp[K, S], two: Inp[K, T]) extends Expr[K, APair[K, S, T]]
