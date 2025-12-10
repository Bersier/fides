package fides.syntax.meta

import fides.syntax.machinery.*

/**
  * Wraps a value into a Quoted.
  */
final case class Wrap[
  D <: TopD,
  G <: ExprHG[D], Q <: TopQ,
  M <: ConsHM[G],
](value: Code[M]) extends Code[WrapM[D, G, Q, M]]

/**
  * Evaluates a quoted expression.
  */
final case class Eval[
  D <: TopD, QQ <: TopQ,
  G <: ExprG[QuoteD[ConsM[ExprHG[D]]]], Q <: TopQ,
  M <: ConsHM[G],
](value: Code[M]) extends Code[EvalM[D, QQ, G, Q, M]]

/* todo
 *
 * With the expanded power of metaprogramming, I think that now,
 * the Q associated with a quote is not as directly related to the Q of the polar of the quote.
 *
 * Actually, the system we have simply cannot support this power :S We need to move to located quotes.
 */
