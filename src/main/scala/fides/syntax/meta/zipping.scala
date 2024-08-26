package fides.syntax.meta

import fides.syntax.code.{Code, CodeType, Expr, Polar, Polarity, Ptrn, ValType, Xctr}
import fides.syntax.values.CollectedG
import Polarity.{Negative, Positive}

/**
  * Used for unordered collections of pieces of code, at the syntax level.
  */
type Args[+S <: CodeType] = ArgsG[Boolean, S]
object Args:
  def apply[S <: CodeType](): ArgsG[false, Nothing] = None
  def apply[S <: CodeType](first: Code[S], others: Code[S]*): ArgsG[true, S] = new Some(first, others*)
  case object None extends ArgsG[false, Nothing]:
    def arguments: Iterable[Code[Nothing]] = Iterable.empty
  type None = None.type
  final class Some[+S <: CodeType](first: Code[S], others: Code[S]*) extends ArgsG[true, S]:
    val arguments: Iterable[Code[S]] = first +: others
  end Some
end Args

sealed trait ArgsG[+IsNonEmpty <: Boolean, +S <: CodeType] extends Code[ArgsG[IsNonEmpty, S]], CodeType:
  def arguments: Iterable[Code[S]]
  override def toString: String = s"Args($arguments)"
end ArgsG

/**
  * Converts a Collected of code quotations to a Quoted of a VarArgs of all the pieces of code.
  */
type Zip[
  IsNonEmpty <: Boolean,
  S <: CodeType,
] = ZipP[Positive, IsNonEmpty, Boolean, S, CodeType, Quoted[ArgsG[IsNonEmpty, S]], ValType]
object Zip:
  inline def apply[IsNonEmpty <: Boolean, S <: CodeType](
    inline pieces: Code[Expr[CollectedG[IsNonEmpty, Quoted[S]]]],
  ): Zip[IsNonEmpty, S] = ZipP(pieces)
end Zip

/**
  * Extracts the processes out of a Concurrent process in the context of an irrefutable pattern.
  */
type UnZip[
  IsNonEmpty <: Boolean,
  S <: CodeType,
] = MatchZip[Nothing, IsNonEmpty, Nothing, S, Nothing, Quoted[ArgsG[IsNonEmpty, S]]]
object UnZip:
  inline def apply[IsNonEmpty <: Boolean, S <: CodeType](
    inline pieces: Code[Xctr[CollectedG[IsNonEmpty, Quoted[S]]]],
  ): UnZip[IsNonEmpty, S] = MatchZip(pieces)
end UnZip

/**
  * Extracts the processes out of a Concurrent process in the context of a refutable pattern.
  */
type MatchZip[
  IsNonEmptyP <: IsNonEmptyN,
  IsNonEmptyN <: Boolean,
  P <: N,
  N <: CodeType,
  L >: Nothing <: Quoted[ArgsG[IsNonEmptyP, P]],
  U >: Quoted[ArgsG[IsNonEmptyN, N]] <: ValType,
] = ZipP[Negative, IsNonEmptyP, IsNonEmptyN, P, N, L, U]
object MatchZip:
  inline def apply[
    IsNonEmptyP <: IsNonEmptyN,
    IsNonEmptyN <: Boolean,
    P <: N,
    N <: CodeType,
    L >: Nothing <: Quoted[ArgsG[IsNonEmptyP, P]],
    U >: Quoted[ArgsG[IsNonEmptyN, N]] <: ValType,
  ](
    inline pieces: Code[Ptrn[CollectedG[IsNonEmptyP, Quoted[P]], CollectedG[IsNonEmptyN, Quoted[N]]]],
  )(using
    Quoted[ArgsG[IsNonEmptyP, P]] <:< (L | Quoted[ArgsG[Nothing, Nothing]]),
    (U & Quoted[ArgsG[Boolean, CodeType]]) <:< Quoted[ArgsG[IsNonEmptyN, N]],
  ): MatchZip[IsNonEmptyP, IsNonEmptyN, P, N, L, U] = ZipP(pieces)
end MatchZip
// todo should this really be allowed?

/**
  * General [[Polar]] for zipping.
  */
final case class ZipP[
  R >: Positive & Negative <: Polarity,
  IsNonEmptyP <: IsNonEmptyN,
  IsNonEmptyN <: Boolean,
  P <: N,
  N <: CodeType,
  L >: Nothing <: Quoted[ArgsG[IsNonEmptyP, P]],
  U >: Quoted[ArgsG[IsNonEmptyN, N]] <: ValType,
](
  pieces: Code[Polar[R, CollectedG[IsNonEmptyP, Quoted[P]], CollectedG[IsNonEmptyN, Quoted[N]]]],
)(using
  Quoted[ArgsG[IsNonEmptyP, P]] <:< (L | Quoted[ArgsG[Nothing, Nothing]]),
  (U & Quoted[ArgsG[Boolean, CodeType]]) <:< Quoted[ArgsG[IsNonEmptyN, N]],
) extends Polar[R, L, U]
