package fides.syntax.connectors

import fides.syntax.code.{Atom, Code, CodeType, Expr, Process, Ptrn, Val, ValType, Xctr}
import fides.syntax.meta.Args
import fides.syntax.values.Pulse

/**
  * Tries to match a value to the given pattern. Upon failure, outputs the value to the alternative instead.
  */
final case class Match[T <: ValType](
  pattern: Code[Ptrn[T, T]],
  matchSignal: Code[Xctr[Pulse]] = Ignore(),
  alternative: Code[Xctr[T]] = Ignore(),
) extends Xctr[T]

final case class Switch[T <: ValType, A <: Atom](
  input  : Code[Expr[T]],
  testee : Code[Expr[A]],
  cases  : Code[Args[Case[T, A]]],
  default: Code[Xctr[T]] = Ignore(),
) extends Process

final case class Case[T <: ValType, A <: Atom](
  testValue: Code[Val[A]],
  exctractor: Code[Xctr[T]],
) extends Code[Case[T, A]], CodeType
