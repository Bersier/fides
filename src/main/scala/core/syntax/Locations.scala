package core.syntax

final class Name extends ValT with L[Nothing, Vid[Name]]

sealed trait SignatoryVal extends ValT with L[Nothing, Vid[SignatoryVal]]
final class  SignatoryKey extends SignatoryVal

sealed trait LocVal[+T <: ValT] extends L[Nothing, Adr[T]]
final class  LocKey[+T <: ValT] extends LocVal[T] // Allows broadcasting

/**
  * Can be used as a value, or as an O.
  * However, when used as an O, does not behave like a pattern, but rather like a Loc.
  */
final case class Broadcast[+K <: N, +T <: ValT](address: L[K, LocKey[T]]) extends L[K, Loc[T]]

/**
  * Can be used as a value, or as an O.
  * However, when used as an O, does not behave like a pattern, but rather like a Loc.
  */
final class Start[+K <: N](name: L[K, Vid[Name]]) extends L[K, Loc[U]]
final class Pause[+K <: N](name: L[K, Vid[Name]]) extends L[K, Loc[U]]
final class Dissolve[+K <: N](name: L[K, Vid[Name]]) extends L[K, Loc[U]]
final class Kill[+K <: N](name: L[K, Vid[Name]]) extends L[K, Loc[U]]
final class Move[+K <: N](name: L[K, Vid[Name]]) extends L[K, Loc[Name]]
