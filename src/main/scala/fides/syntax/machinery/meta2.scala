package fides.syntax.machinery

type Top2M = Gen2M[TopW]
sealed trait Gen2M[+W <: TopW] private[machinery]()

final case class Pair2M[
  +W <: TopW,
  +W1 <: TopW, +W2 <: TopW,
  +M1 <: Gen2M[W1], +M2 <: Gen2M[W2],
](m1: M1, m2: M2)(using PairW[W1, W2, W]) extends Gen2M[W]

final case class Quote2M[
  +W <: TopW,
  +W1 <: TopW, +W2 <: TopW,
  +M1 <: Gen2M[W1], +M2 <: Gen2M[W2],
](m1: M1, m2: M2)(using QuoteW[W1, ?, W]) extends Gen2M[W]

//final abstract class Escape2M[
//  SG <: TopG,
//  SM <: Gen2M[SG],
//  +KM <: Gen2M[NameG[TopK]], +M <: GenM[PolarG[QuoteD[SM], TopP]],
//] extends Gen2M[SG]

/*
todo
Reducing escapes

C1 | G1 <: Expr(QuoteD(M')), M' tight
Escape(a, C1) = C | M
Quote(a, ... C ...)  =>  ReducedM = M'

C1 | G1 <: Xctr(? >: QuoteD(M')), M' tight
Escape(a, C1) = C | M G X H
Quote(a, ... C ...)  =>  ReducedM = M', G = m2g(M') & H, provided m2g(M') is in closure(H)
todo do we need something analogous to H, but for M? O? And then have ReducedM = M' & O? Maybe not.
 */
