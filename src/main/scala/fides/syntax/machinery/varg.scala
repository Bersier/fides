package fides.syntax.machinery

sealed trait TopW private[machinery]()
sealed trait `W+`[+F <: TopF] extends TopW
sealed trait `W-`[-F <: TopF] extends TopW
final abstract class `W0`[F <: TopF] extends `W+`[F], `W-`[F]

sealed trait PairW[W1 <: TopW, W2 <: TopW, W <: TopW]
sealed trait SignedPairW:
  given [
    V1 <: TopV, V2 <: TopV, V <: TopV,
    F1 <: PolarF[V1], F2 <: PolarF[V2],
  ] => PairV[V1, V2, V] => PairW[`W+`[F1], `W+`[F2], `W+`[PairF[V, F1, F2]]]
  given [
    V1 <: TopV, V2 <: TopV, V <: TopV,
    F1 >: PolarBotF[V1] <: TopF, F2 >: PolarBotF[V2] <: TopF,
  ] => PairV[V1, V2, V] => PairW[`W-`[F1], `W-`[F2], `W-`[PairF[V, F1, F2]]]
object PairW extends SignedPairW:
  given [
    V1 <: TopV, V2 <: TopV, V <: TopV,
    F1 <: PolarF[V1], F2 <: PolarF[V2],
  ] => PairV[V1, V2, V] => PairW[`W0`[F1], `W0`[F2], `W0`[PairF[V, F1, F2]]]

sealed trait QuoteW[K <: TopW, T1M <: Top2M, W <: TopW]
sealed trait SignedQuoteW:
  given [
    V <: TopV,
    K <: TopK, T1M <: Top2M,
  ] => QuoteV[K, T1M, V] => QuoteW[`W+`[NameF[K]], T1M, `W+`[QuoteF[V, K, T1M]]]
end SignedQuoteW
// todo shouldn't metapolarity be a function of a name?
//  M flows inside out
//  F flows inside out
//  C flows outside in
//  Use two-pronged approach! First allow the building of terms without checking the exact syntax. Resolves M.
//  Then, add a method typecheck to Code. It requires an implicit proof that the code is properly constructed.
//  Syntax-checking happens in those givens, calling each other.