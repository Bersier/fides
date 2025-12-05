package fides.syntax.types

sealed class ID
case object LauncherID extends ID

sealed trait ConsQ[+H <: TopP, +D <: TopQ]
type TopQ = ConsQ[TopP, ?]
final abstract class BotQ extends ConsQ[BotP, BotQ]

/**
  * @tparam P whether a quote of this code can be used as an expression
  * @tparam N whether a quote of this code can be used as an extractor
  * @tparam C whether a quote of this code can be used as a constant
  */
final abstract class Polarity[+P <: TopB, +N <: TopB, +C <: TopB]
type TopP = Polarity[TopB, TopB, TopB]
type BotP = Polarity[BotB, BotB, BotB]

sealed trait Empty
object Empty:
  final abstract class T extends Empty
  final abstract class F extends Empty
end Empty

sealed trait TopB
final abstract class BotB extends TopB

sealed trait TopN
object TopN:
  final abstract class S[+N <: TopN] extends TopN
  final abstract class Z extends TopN
  type `0` = Z
  type `1` = S[Z]
end TopN
