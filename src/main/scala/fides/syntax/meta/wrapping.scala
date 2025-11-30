package fides.syntax.meta

import fides.syntax.types.{BotT, Code, Expr, Exvr, Polr, Povr, QuotedT, TopT}

/**
  * As an Expr, wraps a value into a Quoted.
  *
  * As an Xctr, unwraps a quoted value.
  */
final case class Wrap[P >: BotT, N <: TopT](value: Code[Polr[P, N]]) extends Code[Povr[P, N]]

/**
  * Evaluates a quoted expression.
  */
final case class Eval[T <: TopT](value: Code[Expr[QuotedT[Expr[T]]]]) extends Code[Exvr[T]]
