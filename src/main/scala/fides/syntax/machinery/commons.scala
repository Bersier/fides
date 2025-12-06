package fides.syntax.machinery

sealed class ID
case object LauncherID extends ID

/**
  * Polarity stack for all meta-levels
  *
  * @tparam H polarity at first code level
  * @tparam D remaining/meta polarities
  */
sealed trait ConsQ[+H <: TopP, +D <: TopQ]
type TopQ = ConsQ[TopP, ?]
final abstract class BotQ extends ConsQ[BotP, BotQ]

/**
  * Polarity
  *
  * @tparam Positive whether a quote of this code can be used as an expression
  * @tparam Negative whether a quote of this code can be used as an extractor
  * @tparam Constant whether a quote of this code can be used as a constant
  */
final abstract class GenP[+Positive <: TopB, +Negative <: TopB, +Constant <: TopB]
type TopP = GenP[TopB, TopB, TopB]
type BotP = GenP[BotB, BotB, BotB]

/**
  * Type-level Boolean used to keep track of whether a collection is empty
  */
sealed trait TopE
object TopE:
  final abstract class T extends TopE
  final abstract class F extends TopE
end TopE

sealed trait TopB
final abstract class BotB extends TopB

sealed trait TopN
object TopN:
  final abstract class G[+N <: TopN] extends TopN
  final abstract class Z extends TopN
  type `0` = Z
  type `1` = G[Z]
end TopN
