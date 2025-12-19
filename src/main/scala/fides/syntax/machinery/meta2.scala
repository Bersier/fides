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

todo isn't ReducedG = G? In that case, shouldn't it be directly derivable from ReducedM via m2g,
 without requiring additional transformation?
 If so, the additional transformation would have to be lifted to be applied to M' somehow.
todo shouldn't W be involved somehow? So the G can adjust based on required variance as needed?
 Well, isn't that's why the given for the neutrals would have higher priority,
 but the others can also apply to a Ntrl? So no W, but polarity is taken into account.

C1 || G1 <: Expr(? <: QuoteD(M')), M' tight
Escape(a, C1) = C || M G H
QuoteExpr(a, ... C ...)  =>
  ReducedM = M', G = +m2g(M') | -H; requiring +m2g(M') <: +H

C1 || G1 <: Xctr(? >: QuoteD(M')), M' tight
Escape(a, C1) = C || M G H
QuoteXctr(a, ... C ...)  =>
  ReducedM = M', G = +m2g(M') & +H; requiring +m2g(M') >: -H

C1 || G1 <: Ntrl(? =: QuoteD(M')), M' tight
Escape(a, C1) = C || M G H
QuoteNtrl(a, ... C ...)  =>
  ReducedM = M', G = +m2g(M'); requiring -H <: m2g(M') <: +H

----

C1 || G1 <: Expr(? <: QuoteD(M')), M' tight
Escape(a, C1) = C || M G H
QuoteExpr(a, ... QuoteExpr(b, ... C ...) ...)  =>
  ReducedM = M', G = +m2g(M') | -H; requiring +m2g(M') <: +H

C1 || G1 <: Expr(? <: QuoteD(M')), M' tight
Escape(a, C1) = C || M G H
QuoteExpr(a, ... QuoteXctr(b, ... C ...) ...)  =>
  ReducedM = M', G = -m2g(M') & +H; requiring +m2g(M') <: +H

C1 || G1 <: Xctr(? >: QuoteD(M')), M' tight
Escape(a, C1) = C || M G H
QuoteXctr(a, ... QuoteExpr(b, ... C ...) ...)  =>
  ReducedM = M', G = +m2g(M') & +H; requiring +m2g(M') >: -H

C1 || G1 <: Xctr(? >: QuoteD(M')), M' tight
Escape(a, C1) = C || M G H
QuoteXctr(a, ... QuoteXctr(b, ... C ...) ...)  =>
  ReducedM = M', G = -m2g(M') | -H; requiring +m2g(M') >: -H

C1 || G1 <: Ntrl(? =: QuoteD(M')), M' tight
Escape(a, C1) = C || M G H
QuoteNtrl(a, ... QuoteExpr(b, ... C ...) ...)  =>
  ReducedM = M', G = +m2g(M'); requiring -H <: m2g(M') <: +H

C1 || G1 <: Ntrl(? =: QuoteD(M')), M' tight
Escape(a, C1) = C || M G H
QuoteNtrl(a, ... QuoteXctr(b, ... C ...) ...)  =>
  ReducedM = M', G = -m2g(M'); requiring -H <: m2g(M') <: +H

----

 */
