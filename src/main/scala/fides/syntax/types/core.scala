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

type TopP = Polarity[Bool.F, Bool.F, Bool.F]
final abstract class Polarity[+P <: Bool, +N <: Bool, +C <: Bool]
type BotP = Polarity[Bool.T, Bool.T, Bool.T]

sealed class ID
case object LauncherID extends ID
