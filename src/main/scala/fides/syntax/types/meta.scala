package fides.syntax.types

import util.TopN

/**
  * General type to represent Fides code
  */
trait Code[+S <: TopS, +M <: TopM] private[syntax]() extends OldCode[S]
type TopC = Code[TopS, TopM]

sealed trait TopM
sealed trait SomeM[+H <: TopP, +T <: TopM] extends TopM
final abstract class BotM extends SomeM[BotP, BotM]

// todo delete
type RepM[H <: TopP, Count <: TopN, T <: TopM] <: TopM = Count match
  case TopN.`0` => T
  case TopN.S[pred] => SomeM[H, RepM[H, pred, T]]

sealed trait RepR[H <: TopP, Count <: TopN, T <: TopM, R <: TopM]
object RepR:
  given [H <: TopP, T <: TopM] => RepR[H, TopN.`0`, T, T]
  given [
    H <: TopP, CountMinusOne <: TopN, T <: TopM, R <: TopM,
  ] => RepR[H, CountMinusOne, T, R] => RepR[H, TopN.S[CountMinusOne], T, SomeM[H, R]]
end RepR
