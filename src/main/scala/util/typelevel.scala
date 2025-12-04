package util

sealed trait TopB
object TopB:
  type F = TopB
  final abstract class T extends TopB
end TopB

sealed trait TopN
object TopN:
  final abstract class S[+N <: TopN] extends TopN
  final abstract class `0` extends TopN
  type `1` = S[`0`]
end TopN
