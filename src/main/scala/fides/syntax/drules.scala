package fides.syntax

sealed trait VariantDR[
  -Key >: BotK <: TopK, -Value >: `BotD:` <: `TopD:`,
  SelfD >: `BotD:` <: `TopD:`,
]
sealed trait VariantLDR:
  given [Key >: BotK <: TopK, Value >: BotD <: TopD] => VariantDR[Key, `D+`[Value], `D+`[VariantD[Key, Value]]]
  given [Key >: BotK <: TopK, Value >: BotD <: TopD] => VariantDR[Key, `D-`[Value], `D-`[VariantD[Key, Value]]]
object VariantDR extends VariantLDR:
  given [Key >: BotK <: TopK, Value >: BotD <: TopD] => VariantDR[Key, `D0`[Value], `D0`[VariantD[Key, Value]]]
end VariantDR