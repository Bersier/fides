package fides.syntax.meta

import fides.syntax.core.Code
import fides.syntax.types.{ArgsS, CodeType, CollectedT, OffBot, Polar, QuotedT}

/**
  * Used for unordered collections of pieces of code, at the syntax level.
  */
object Args:
  def apply[S <: CodeType](): None = Empty
  def apply[S <: CodeType](first: Code[S], others: Code[S]*): Some[S] = new NonEmpty(first, others*)

  type None = Code[ArgsS[false, OffBot]]
  type Some[+S <: CodeType] = Code[ArgsS[true, S]]

  private case object Empty extends None:
    def arguments: Iterable[Code[OffBot]] = Iterable.empty
  private final class NonEmpty[+S <: CodeType](first: Code[S], others: Code[S]*) extends Some[S]:
    val arguments: Iterable[Code[S]] = first +: others
end Args

/**
  * As an Expr, converts a collection of code quotations to a [[Quoted]] of [[Args]] of all the pieces of code.
  *
  * As an Xctr, extracts the arguments out of a [[Quoted]] of [[Args]].
  */
final case class Zip[
  IsNonEmptyP <: Boolean,
  IsNonEmptyN <: Boolean,
  P <: CodeType,
  N <: CodeType,
](
  pieces: Code[Polar[CollectedT[IsNonEmptyP, QuotedT[P]], CollectedT[IsNonEmptyN, QuotedT[N]]]],
) extends Code[Polar[QuotedT[ArgsS[IsNonEmptyP, P]], QuotedT[ArgsS[IsNonEmptyN, N]]]]
