package fides.syntax.connectors

import fides.syntax.code.{Atom, Code, CodeType, Expr, Process, Ptrn, Val, ValType, Xctr}
import fides.syntax.meta.Args
import fides.syntax.values.Collected

/**
  * Tries to match a value to the given pattern. Upon failure, outputs the value to the alternative instead.
  *
  * Unfortunately, it doesn't seem possible to refine the type of [[alternative]] in Scala.
  */
final case class Match[T <: ValType](
  pattern: Code[Ptrn[T, T]],
  matchedValue: Code[Xctr[T]] = Ignore(),
  alternative: Code[Xctr[T]] = Ignore(),
) extends Xctr[T]

// todo no given instance of (T <:< (pattern$1 : fides.syntax.meta.MatchQuote[S]) | T) was found...
//    alternative: Code[Xctr[S]] = Ignore())(using T <:< pattern.type | S) extends Xctr[T]

// todo delete; match types never seem to work. The plan was to use this to refine the type of a match alternative to
//  Code[Xctr[AltType[T, pattern.type]]]
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
