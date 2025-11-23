package fides.syntax.meta

import fides.syntax.core.Code
import fides.syntax.types.{ArgsS, CollectedT, OffBotT, Polr, Povr, QuotedT, TopS}
import observable.{Async, DefiniteObserver, FiniteObservable}

/**
  * Used for unordered collections of pieces of code, at the syntax level.
  */
sealed trait Args[+S <: TopS] extends Code[ArgsS[Boolean, S]]:
  def arguments: FiniteObservable[Code[S]] // todo should be Multiset[Code[S]]
object Args:
  def apply[S <: TopS](): None = Empty
  def apply[S <: TopS](first: Code[S], others: Code[S]*): Some[S] = new NonEmpty(first, others*)
  def unapply[S <: TopS](arguments: Args[S]): scala.Some[FiniteObservable[Code[S]]] =
    Some(arguments.arguments)

  type None = Code[ArgsS[false, OffBotT]]
  type Some[+S <: TopS] = Code[ArgsS[true, S]]

  private case object Empty extends Args[OffBotT], None:
    def arguments: FiniteObservable[Code[OffBotT]] =
      (observer: DefiniteObserver[Code[OffBotT]]) => observer.withSize(0)
  private final class NonEmpty[+S <: TopS](first: Code[S], others: Code[S]*) extends Args[S], Some[S]:
    val arguments: FiniteObservable[Code[S]] = new FiniteObservable[Code[S]]:
      private val arguments = first +: others

      def foreach(observer: DefiniteObserver[Code[S]]): Async =
        for argument <- arguments do observer.update(argument)
        observer.withSize(arguments.size)
end Args

/**
  * As an Expr, converts a collection of code quotations to a [[Quoted]] of [[Args]] of all the pieces of code.
  *
  * As an Xctr, extracts the arguments out of a [[Quoted]] of [[Args]].
  */
final case class Zip[
  IsNonEmptyP <: Boolean,
  IsNonEmptyN <: Boolean,
  P <: TopS,
  N <: TopS,
](
  pieces: Code[Polr[CollectedT[IsNonEmptyP, QuotedT[P]], CollectedT[IsNonEmptyN, QuotedT[N]]]],
) extends Code[Povr[QuotedT[ArgsS[IsNonEmptyP, P]], QuotedT[ArgsS[IsNonEmptyN, N]]]]
