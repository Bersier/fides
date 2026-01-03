package fides.syntax

// todo fix
sealed trait BeforeER[-E1 >: `-E`[TopD] <: `+E`[TopD], +E2 >: `-E`[TopD] <: `+E`[TopD]]
object BeforeER:
  given [
    E1 >: `-E`[TopD] <: `+E`[TopD], E2 >: `-E`[TopD] <: `+E`[TopD],
  ] => ESubtypeR[E1, E2] => BeforeER[E1, E2]
  given [D1 <: TopD, D2 <: TopD] => BeforeDR[D1, D2] => BeforeER[`+E`[D1], `+E`[D2]]
  given [D1 <: TopD, D2 <: TopD] => BeforeDR[D1, D2] => BeforeER[`-E`[D1], `+E`[D2]]
  given [D1 <: TopD, D2 <: TopD] => BeforeDR[D1, D2] => BeforeER[`-E`[D1], `-E`[D2]]
end BeforeER

// todo delete
sealed trait BeforeDR[-D1 <: TopD, +D2 <: TopD]
object BeforeDR:
  given [D1 <: D2, D2 <: TopD] => BeforeDR[D1, D2]
  given [D1 <: TopD, D2 <: TopD, D3 <: TopD] => NextDR[D1, D2] => BeforeDR[D2, D3] => BeforeDR[D1, D3]
end BeforeDR

// todo should take Es
private sealed trait NextDR[-D1 <: TopD, +D2 <: TopD]
object NextDR:
  given NextDR[AddressD[TopK, `-E`[TopD]], BehaviorD[`-H`[XpolarG]]]
  given NextDR[FalseD, TrueD]
  given NextDR[BehaviorD[`+H`[XpolarG]], CertificateD[BotK, `-E`[TopD]]]
  given NextDR[CertificateD[TopK, `+E`[TopD]], IdentifierD[BotK]]
  given NextDR[IdentifierD[TopK], NatD[BotN]]
  given NextDR[NatD[TopN], PreQuoteD[BotM]]
  given NextDR[PreQuoteD[TopM], PulseD]
  given NextDR[PulseD, QuoteD[TopM]]
  given NextDR[QuoteD[BotM], EntryD[TopK, `+E`[TopD]]]
  // todo add more cases and fix; should be fractal
end NextDR
