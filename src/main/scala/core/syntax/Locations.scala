package core.syntax

sealed trait ID[+K <: N, +C <: Ide[_]] extends ValT with L[K, C]

final class Name extends ID[Nothing, Val[Name]]

sealed trait SignatoryVal extends ID[Nothing, Val[SignatoryVal]]
final class  SignatoryKey extends SignatoryVal

sealed trait LocVal[+T <: ValT] extends ID[Nothing, Loc[T]]
final class  LocKey[+T <: ValT] extends LocVal[T] // Allows broadcasting

/**
  * Can be used as a value, or as an O.
  * However, when used as an O, does not behave like a pattern, but rather like a Loc.
  */
final case class Broadcast[+K <: N, +T <: X[K, D, T]](address: L[K, LocKey[T]]) extends V[K, T]

/**
  * Can be used as a value, or as an O.
  * However, when used as an O, does not behave like a pattern, but rather like a Loc.
  */
sealed trait Command[+K <: N, +T <: X[K, Val, T]] extends V[K, T]
final class Start[+K <: N](name: V[K, Name]) extends Command[K, U]
final class Pause[+K <: N](name: V[K, Name]) extends Command[K, U]
final class Dissolve[+K <: N](name: V[K, Name]) extends Command[K, U]
final class Kill[+K <: N](name: V[K, Name]) extends Command[K, U]
final class Move[+K <: N](name: V[K, Name]) extends Command[K, Name]
