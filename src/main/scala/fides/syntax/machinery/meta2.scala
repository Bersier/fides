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

C1 || G1 <: Expr(? <: QuoteD(M')), M' tight
Escape(a, C1) = C || M G H
QuoteExpr(a, ... QuoteNtrl(b, ... C ...) ...)  =>
  ReducedM = M', G = |span(m2g(M') | -H); requiring m2g(M') <: +H

C1 || G1 <: Xctr(? >: QuoteD(M')), M' tight
Escape(a, C1) = C || M G H
QuoteXctr(a, ... QuoteExpr(b, ... C ...) ...)  =>
  ReducedM = M', G = +m2g(M') & +H; requiring +m2g(M') >: -H

C1 || G1 <: Xctr(? >: QuoteD(M')), M' tight
Escape(a, C1) = C || M G H
QuoteXctr(a, ... QuoteXctr(b, ... C ...) ...)  =>
  ReducedM = M', G = -m2g(M') | -H; requiring +m2g(M') >: -H

C1 || G1 <: Xctr(? >: QuoteD(M')), M' tight
Escape(a, C1) = C || M G H
QuoteXctr(a, ... QuoteNtrl(b, ... C ...) ...)  =>
  ReducedM = M', G = &span(m2g(M') & +H); requiring m2g(M') >: -H

C1 || G1 <: Ntrl(? =: QuoteD(M')), M' tight
Escape(a, C1) = C || M G H
QuoteNtrl(a, ... QuoteExpr(b, ... C ...) ...)  =>
  ReducedM = M', G = +m2g(M'); requiring -H <: m2g(M') <: +H

C1 || G1 <: Ntrl(? =: QuoteD(M')), M' tight
Escape(a, C1) = C || M G H
QuoteNtrl(a, ... QuoteXctr(b, ... C ...) ...)  =>
  ReducedM = M', G = -m2g(M'); requiring -H <: m2g(M') <: +H

C1 || G1 <: Ntrl(? =: QuoteD(M')), M' tight
Escape(a, C1) = C || M G H
QuoteNtrl(a, ... QuoteNtrl(b, ... C ...) ...)  =>
  ReducedM = M', G = +m2g(M'); requiring +m2g(M') in span(H)

todo this was written simply by following the patterns. We need theoretical justification.

----

 */

// todo For QuoteExpr(a, ... QuoteNtrl(b, ... C ...) ...),
//  what if the inner QuoteNtrl doesn't have to be neutral from the context?
//  For example, maybe it's only used as an Expr. In that case, the rule for
//  QuoteExpr(a, ... QuoteExpr(b, ... C ...) ...) should be considered as well, right? Or not?
//  Perhaps the polarity of the quote should only be determined from its contents,
//  plus possibly from some explicit annotation.
//  Yes, I don't think the polarity of the nested quote should be restricted
//  because of some external escape that cannot accommodate it. Quote polarity determination should be local.
//  So we should probably add some primitives to restrict the polarity of a polar,
//  that otherwise behaves like the identity? It could even be a single bipolar that,
//  depending on whether it's applied or deplied, restricts the polar to an expression or an extractor.
