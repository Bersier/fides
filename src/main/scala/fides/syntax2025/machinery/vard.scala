package fides.syntax2025.machinery

sealed trait TopV private[machinery]()
sealed trait `V+`[+D >: BotD <: TopD] extends TopV
sealed trait `V-`[-D >: BotD <: TopD] extends TopV
final abstract class`V0`[D >: BotD <: TopD] extends `V+`[D], `V-`[D]
// todo we want to be able to express the intersection of `V0`[D] over all D.
//  We can write it out explicitly, I think: `V0`[TrueD] & `V0`[FalseD] & `V0`[NatD[Nothing]] & ...
//  So we could have sealed trait PolarBottomF extends PolarBotF[`V0`[TrueD] & ...], or something
//  Similarly for other such cases (see `W0`).

sealed trait PairV[-V1 <: TopV, -V2 <: TopV, V <: TopV]
sealed trait SignedPairV: // todo rename
  given [D1 >: BotD <: TopD, D2 >: BotD <: TopD] => PairV[`V+`[D1], `V+`[D2], `V+`[PairD[D1, D2]]]
  given [D1 >: BotD <: TopD, D2 >: BotD <: TopD] => PairV[`V-`[D1], `V-`[D2], `V-`[PairD[D1, D2]]]
object PairV extends SignedPairV:
  given [D1 >: BotD <: TopD, D2 >: BotD <: TopD] => PairV[`V0`[D1], `V0`[D2], `V0`[PairD[D1, D2]]]
end PairV


sealed trait QuoteV[K <: TopK, T1M <: Top2M, V <: TopV]
sealed trait SignedQuoteV:
  given [K <: TopK, T1M <: Top2M, T2M <: Top2M] => ExprReduction[K, T1M, T2M] => QuoteV[K, T1M, `V+`[Quote2D[T2M]]]
