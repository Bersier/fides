package util

import typelevelnumbers.binary.{Bit, Bits}

sealed trait BoolT
final abstract class FlseT extends BoolT
final abstract class TrueT extends BoolT

sealed trait TopB
final abstract class BotB extends TopB

sealed trait TopN
object TopN:
  final abstract class S[+N <: TopN] extends TopN
  final abstract class Z extends TopN
  type `0` = Z
  type `1` = S[Z]
end TopN
