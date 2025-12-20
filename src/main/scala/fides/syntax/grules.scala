package fides.syntax

sealed trait VariantGR[
  -Key >: BotK <: TopK, -Value >: `-PolarG` <: PolarG[`TopD:`],
  SelfD >: `BotD:` <: `TopD:`,
]
object VariantGR:
  given [
    ValueType >: `BotD:` <: `TopD:`,
    Key >: BotK <: TopK, Value >: `-PolarG` <: PolarG[ValueType],
    SelfD >: `BotD:` <: `TopD:`,
  ] => VariantDR[Key, ValueType, SelfD] => VariantGR[Key, Value, SelfD]
end VariantGR
