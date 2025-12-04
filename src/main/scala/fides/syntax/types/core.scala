package fides.syntax.types

import fides.syntax.types.*
import util.Bool

import scala.language.experimental.pureFunctions

/**
  * General type to represent Fides code
  */
trait Code[+S <: TopS] private[syntax]()

trait Code2[S <: TopS, +M <: TopM] extends Code[S]
type NtrlC[T <: TopT] = Code2[Ntrl2[T], BotM]

trait Code3[C <: TopC] private[syntax]()
// todo perhaps we can get rid of this wrapper entirely

sealed trait TopM
sealed trait SomeM[+H <: TopP, +T <: TopM] extends TopM
final abstract class BotM extends SomeM[BotP, BotM]

/**
  * @tparam P whether a quote of this code can be used as an expression
  * @tparam N whether a quote of this code can be used as an extractor
  * @tparam C whether a quote of this code can be used as a constant
  */
final abstract class Polarity[+P <: Bool, +N <: Bool, +C <: Bool]
type TopP = Polarity[Bool.F, Bool.F, Bool.F]
type BotP = Polarity[Bool.T, Bool.T, Bool.T]

sealed class ID
case object LauncherID extends ID
