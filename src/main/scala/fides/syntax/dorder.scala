package fides.syntax

sealed trait BeforeDR[-D1 >: BotD <: TopD, +D2 >: BotD <: TopD]
object BeforeDR:
  given [D1 >: BotD <: D2, D2 >: BotD <: TopD] => BeforeDR[D1, D2]
  given [
    D1 >: BotD <: TopD, D2 >: BotD <: TopD, D3 >: BotD <: TopD,
  ] => NextDR[D1, D2] => BeforeDR[D2, D3] => BeforeDR[D1, D3]
end BeforeDR

private sealed trait NextDR[-D1 >: BotD <: TopD, +D2 >: BotD <: TopD]
object NextDR:
  given NextDR[AddressD[TopK, BotD], BehaviorD[`-H`[XpolarG]]]
  given NextDR[BehaviorD[`+H`[XpolarG]], CertificateD[BotK, BotD]]
  given NextDR[CertificateD[TopK, TopD], IdentifierD[BotK]]
  given NextDR[IdentifierD[TopK], NatD[BotN]]
  given NextDR[NatD[TopN], PreQuoteD[BotM]]
  given NextDR[PreQuoteD[TopM], PulseD]
  given NextDR[PulseD, QuoteD[TopM]]
  given NextDR[QuoteD[BotM], EntryD[TopK, TopD]]
  // todo add more cases
end NextDR
