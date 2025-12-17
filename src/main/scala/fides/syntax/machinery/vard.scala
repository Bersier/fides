package fides.syntax.machinery

sealed trait TopV private[machinery]()
sealed trait `V+`[+D >: BotD <: TopD] extends TopV
sealed trait `V-`[-D >: BotD <: TopD] extends TopV
final abstract class`V0`[D >: BotD <: TopD] extends `V+`[D], `V-`[D]

sealed trait PairV[V1 <: TopV, V2 <: TopV, PV <: TopV]
sealed trait SignedPairV:
  given [D1 >: BotD <: TopD, D2 >: BotD <: TopD] => PairV[`V+`[D1], `V+`[D2], `V+`[PairD[D1, D2]]]
  given [D1 >: BotD <: TopD, D2 >: BotD <: TopD] => PairV[`V-`[D1], `V-`[D2], `V-`[PairD[D1, D2]]]
object PairV extends SignedPairV:
  given [D1 >: BotD <: TopD, D2 >: BotD <: TopD] => PairV[`V0`[D1], `V0`[D2], `V0`[PairD[D1, D2]]]
