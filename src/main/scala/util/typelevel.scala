package util

import typelevelnumbers.binary.{Bit, Bits}

sealed trait TopB
final abstract class BotB extends TopB

sealed trait TopN
object TopN:
  final abstract class S[+N <: TopN] extends TopN
  final abstract class `0` extends TopN
  type `1` = S[`0`]
  
  sealed trait ReprR[N <: TopN, B <: Bits]
  object ReprR:
    given ReprR[TopN.`0`, Bits.None]
    given [N <: TopN, B <: Bits, D <: TopN] => ReprR[N, B] => DoubleR[N, D] => ReprR[D, Bits.Some[Bit.O, B]]
    given [N <: TopN, B <: Bits, D <: TopN] => ReprR[N, B] => DoubleR[N, D] => ReprR[TopN.S[D], Bits.Some[Bit.I, B]]
  end ReprR
  
  sealed trait DoubleR[N <: TopN, D <: TopN]
  object DoubleR:
    given DoubleR[TopN.`0`, TopN.`0`]
    given [N <: TopN, D <: TopN] => DoubleR[N, D] => DoubleR[TopN.S[N], TopN.S[TopN.S[D]]]
  end DoubleR
end TopN
