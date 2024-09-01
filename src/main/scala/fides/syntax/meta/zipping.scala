package fides.syntax.meta

import fides.syntax.code.Polarity.{Negative, Positive}
import fides.syntax.code.{Code, CodeType, Expr, Polar, Polarity, ValType, Xctr}
import fides.syntax.values.CollectedG
import util.&:&

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
  * Converts a collection of code quotations to a [[Quoted]] of [[Args]] of all the pieces of code.
  *
  * [[Zip]]`[IsNonEmpty, S] <: `[[Expr]]`[`[[Quoted]]`[`[[ArgsG]]`[IsNonEmpty, S]]]`
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
  * Extracts the arguments out of a [[Quoted]] of [[Args]] in the context of an irrefutable pattern.
  *
  * [[UnZip]]`[IsNonEmpty, S] <: `[[Xctr]]`[`[[Quoted]]`[`[[ArgsG]]`[IsNonEmpty, S]]]`
  */
type UnZip[
  IsNonEmpty <: Boolean,
  S <: CodeType,
] = ZipP[Negative, Nothing, IsNonEmpty, Nothing, S, Nothing, Quoted[ArgsG[IsNonEmpty, S]]]
object UnZip:
  inline def apply[IsNonEmpty <: Boolean, S <: CodeType](
    inline pieces: Code[Xctr[CollectedG[IsNonEmpty, Quoted[S]]]],
  ): UnZip[IsNonEmpty, S] = ZipP(pieces)
end UnZip

/**
  * General [[Polar]] for zipping. Note that it can only be an [[Expr]] or an [[Xctr]].
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
  (R =:= Positive) | ((R =:= Negative) &:& (P =:= Nothing)),
  Quoted[ArgsG[IsNonEmptyP, P]] <:< (L | Quoted[ArgsG[Nothing, Nothing]]),
  (U & Quoted[ArgsG[Boolean, CodeType]]) <:< Quoted[ArgsG[IsNonEmptyN, N]],
) extends Polar[R, L, U]
