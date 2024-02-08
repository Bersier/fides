package fides.syntax.connectors

import fides.syntax.code.{Atom, Code, CodeType, Expr, Process, Ptrn, Val, ValType, Xctr}
import fides.syntax.meta.Args
import fides.syntax.values.Collected

/**
  * Tries to match a value to the given pattern. Upon failure, outputs the value to the alternative instead.
  *
  * Unfortunately, it doesn't seem possible to refine the type of [[alternative]] in Scala.
  */
final case class Match[T <: ValType](pattern: Code[Ptrn[T, T]], alternative: Code[Xctr[T]] = Ignore()) extends Xctr[T]

/**
  * Upon matching, copies the matched value to [[matchedValue]].
  */
final case class CopyMatch[P <: N, N <: ValType](
  pattern: Code[Ptrn[P, N]],
  matchedValue: Code[Xctr[N]],
) extends Ptrn[P, N]

final case class Match2[T <: ValType, A <: T](
  pattern: Code[Ptrn[T, T]],
  matchedValue: Code[Xctr[T]] = Ignore(),
  alternative: Code[Xctr[A]] = Ignore(),
)(using T <:< (pattern.type | A)) extends Xctr[T]

final class Match3[T <: ValType](
  val pattern: Code[Ptrn[T, T]],
  val matchedValue: Code[Xctr[T]] = Ignore(),
  val alternative: Code[Xctr[AltType[T, pattern.type]]] = Ignore(),
) extends Xctr[T]

private type AltType[T <: ValType, P <: Code[Ptrn[T, T]]] <: T = T match
  case Collected[?] => P match
    case Collected.None => Collected.Some[?] & T
    case Collected.Some[?] => Collected.None & T
    case _ => T
  case _ => T

final case class Switch[T <: ValType, A <: Atom](
  input  : Code[Expr[T]],
  testee : Code[Expr[A]],
  cases  : Code[Args[Case[T, A]]],
  default: Code[Xctr[T]] = Ignore(),
) extends Process

final case class Case[T <: ValType, A <: Atom](
  testValue: Code[Val[A]],
  extractor: Code[Xctr[T]],
) extends Code[Case[T, A]], CodeType
