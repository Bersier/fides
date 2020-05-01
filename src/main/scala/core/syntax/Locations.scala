package core.syntax

final class Name extends ValT with L[Nothing, Vid[Name]]

sealed trait SignatoryVal extends ValT with L[Nothing, Vid[SignatoryVal]]
final class  SignatoryKey extends SignatoryVal

sealed trait LocVal[+T <: ValT] extends L[Nothing, Adr[T]]
final class  LocKey[+T <: ValT] extends LocVal[T] // Allows broadcasting

final case class Broadcast[+K <: N, +T <: ValT](address: L[K, LocKey[T]]) extends L[K, Loc[T]]

final case class Start[+K <: N](name: L[K, Vid[Name]]) extends L[K, Loc[U]]
final case class Pause[+K <: N](name: L[K, Vid[Name]]) extends L[K, Loc[U]]
final case class Dissolve[+K <: N](name: L[K, Vid[Name]]) extends L[K, Loc[U]]
final case class Kill[+K <: N](name: L[K, Vid[Name]]) extends L[K, Loc[U]]
final case class Move[+K <: N](name: L[K, Vid[Name]]) extends L[K, Loc[Name]]
