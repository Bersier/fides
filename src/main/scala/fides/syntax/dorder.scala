package fides.syntax

// todo fix, and rename
sealed trait BeforeER[-E1 >: ND[TopD] <: PD[TopD], +E2 >: ND[TopD] <: PD[TopD]]
object BeforeER:
  given [
    E1 >: ND[TopD] <: PD[TopD], E2 >: ND[TopD] <: PD[TopD],
  ] => DSubtypeR[E1, E2] => BeforeER[E1, E2]
  given [D1 <: TopD, D2 <: TopD] => BeforeDR[D1, D2] => BeforeER[PD[D1], PD[D2]]
  given [D1 <: TopD, D2 <: TopD] => BeforeDR[D1, D2] => BeforeER[ND[D1], PD[D2]]
  given [D1 <: TopD, D2 <: TopD] => BeforeDR[D1, D2] => BeforeER[ND[D1], ND[D2]]
end BeforeER

// todo delete
sealed trait BeforeDR[-D1 <: TopD, +D2 <: TopD]
object BeforeDR:
  given [D1 <: D2, D2 <: TopD] => BeforeDR[D1, D2]
  given [D1 <: TopD, D2 <: TopD, D3 <: TopD] => NextDR[D1, D2] => BeforeDR[D2, D3] => BeforeDR[D1, D3]
end BeforeDR

// todo should take PDs
private sealed trait NextDR[-D1 <: TopD, +D2 <: TopD]
object NextDR:
  given NextDR[AddressD[TopK, ND[TopD]], BehaviorD[NG[XpolarG]]]
  given NextDR[FalseD, TrueD]
  given NextDR[BehaviorD[PG[XpolarG]], CertificateD[BotK, ND[TopD]]]
  given NextDR[CertificateD[TopK, PD[TopD]], IdentifierD[BotK]]
  given NextDR[IdentifierD[TopK], NatD[BotN]]
  given NextDR[NatD[TopN], PreQuoteD[BotM]]
  given NextDR[PreQuoteD[TopM], PulseD]
  given NextDR[PulseD, QuoteD[TopM]]
  given NextDR[QuoteD[BotM], EntryD[TopK, PD[TopD]]]
  // todo add more cases and fix; should be fractal
end NextDR
