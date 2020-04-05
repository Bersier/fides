package core.syntax

sealed trait Expr[+K <: I, +T <: V[K]] extends Inp[K, T]

final case class Constant[K <: I, T <: V[K]](value: T) extends Expr[K, T]

final case class Join[K <: I, S <: V[K], T <: V[K]](one: Inp[K, S], two: Inp[K, T]) extends Expr[K, APair[K, S, T]]
