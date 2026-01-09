package fides.syntax

import scala.util.NotGiven

sealed trait BeforeDR[D1 >: ND[TopD] <: PD[TopD], D2 >: ND[TopD] <: PD[TopD]]
object BeforeDR:
  given [
    D1 >: ND[TopD] <: PD[TopD],
    D2 >: ND[TopD] <: PD[TopD],
  ] => DSubtypeR[D1, D2] => BeforeDR[D1, D2]
  given LeftSubtypeWeakening: [
    D1 >: ND[TopD] <: PD[TopD],
    D2 >: ND[TopD] <: PD[TopD],
    D3 >: ND[TopD] <: PD[TopD],
  ] => DSubtypeR[D1, D2] => BeforeDR[D2, D3] => BeforeDR[D1, D3]()
  given RightSubtypeWeakening: [
    D1 >: ND[TopD] <: PD[TopD],
    D2 >: ND[TopD] <: PD[TopD],
    D3 >: ND[TopD] <: PD[TopD],
  ] => DSubtypeR[D2, D3] => BeforeDR[D1, D2] => BeforeDR[D1, D3]()
  given LeftChaining: [
    D1 >: ND[TopD] <: PD[TopD],
    D2 >: ND[TopD] <: PD[TopD],
    D3 >: ND[TopD] <: PD[TopD],
  ] => ChainedDR[D1, D2] => BeforeDR[D2, D3] => BeforeDR[D1, D3]()
  given RightChaining: [
    D1 >: ND[TopD] <: PD[TopD],
    D2 >: ND[TopD] <: PD[TopD],
    D3 >: ND[TopD] <: PD[TopD],
  ] => ChainedDR[D2, D3] => BeforeDR[D1, D2] => BeforeDR[D1, D3]()
end BeforeDR
// todo not constructive enough. Search would have to first try to find a local path, then go up, and repeat.
//  Weakening has to be used just right, not too little, not too much.

private sealed trait ChainedDR[D1 >: ND[TopD] <: PD[TopD], D2 >: ND[TopD] <: PD[TopD]]
object ChainedDR:
  given AddressOrder: [
    D1 >: ND[TopD] <: PD[TopD], D2 >: ND[TopD] <: PD[TopD],
    K1 >: BotK <: K2, K2 >: BotK <: TopK,
  ] => NotGiven[K2 <:< K1] => ChainedDR[PD[AddressD[K1, D1]], PD[AddressD[K2, D2]]]()
  given AddressBeforeBehavior: [
    D >: ND[TopD] <: PD[TopD], G >: NG[XpolarG] <: PG[XpolarG],
    Behavior >: ND[BehaviorD] <: PD[BehaviorD],
  ] => ChainedDR[PD[AddressD[TopK, D]], Behavior]()
  given BehaviorBeforeBool: [
    D >: ND[BoolD] <: PD[BoolD],
  ] => ChainedDR[PD[BehaviorD], D]()
  given FalseBeforeTrue: ChainedDR[PD[FalseD], PD[TrueD]]()
  // todo add more cases
end ChainedDR
