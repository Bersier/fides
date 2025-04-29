package fides.syntax.meta

import fides.syntax.core.Code
import fides.syntax.types.{CodeType, Expr, Polar, ValTop, Xctr}
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
  * As an Expr, converts a collection of code quotations to a [[Quoted]] of [[Args]] of all the pieces of code.
  *
  * As an Xctr, extracts the arguments out of a [[Quoted]] of [[Args]].
  */
type Zip[
  IsNonEmpty <: Boolean,
  S <: CodeType,
] = ZipP[Positive, IsNonEmpty, Boolean, S, CodeType, Quoted[ArgsG[IsNonEmpty, S]], ValTop]
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

final case class ZipP[
  IsNonEmptyP >: Boolean,
  IsNonEmptyN <: Boolean,
  P <: N,
  N <: CodeType,
  L >: Nothing <: Quoted[ArgsG[IsNonEmptyP, P]],
  U >: Quoted[ArgsG[IsNonEmptyN, N]] <: ValTop,
](
  pieces: Code[Polar[CollectedG[IsNonEmptyP, Quoted[P]], CollectedG[IsNonEmptyN, Quoted[N]]]],
) extends Polar[L, U]
