package fides.syntax2025.machinery

sealed class TopK
private[machinery] final abstract class LauncherK extends TopK
private[machinery] final abstract class NullK extends TopK

/**
  * Polarity stack for all meta-levels
  *
  * @tparam H polarity at first code level
  * @tparam Q remaining/meta polarities
  */
sealed trait ConsQ[+H <: TopP, +Q <: TopQ]
type TopQ = ConsQ[TopP, ?]
final abstract class BotQ extends ConsQ[BotP, BotQ]
// todo unused; delete?

/**
  * Polarity
  *
  * @tparam Positive whether a quote of this code can be used as an expression
  * @tparam Negative whether a quote of this code can be used as an extractor
  * @tparam Constant whether a quote of this code can be used as a constant
  */
final abstract class GenP[+Positive <: TopB, +Negative <: TopB, +Constant <: TopB]
type TopP = GenP[TopB, TopB, TopB]
type BotVP = GenP[BotB, BotB, TopB]
type BotP = Nothing // todo GenP[BotB, BotB, BotB]

/**
  * Type-level Boolean used to keep track of whether a collection is empty
  */
sealed trait TopE
object TopE:
  final abstract class T extends TopE
  final abstract class F extends TopE
end TopE

/**
  * Type-level representation of Booleans for which unknown is equivalent to false
  */
sealed trait TopB
final abstract class BotB extends TopB

/**
  * Type-level representation of unary natural numbers
  */
sealed trait TopN
object TopN:
  final abstract class S[+N <: TopN] extends TopN
  final abstract class Z extends TopN
  type `0` = Z
  type `1` = S[Z]
end TopN
