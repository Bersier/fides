package core.syntax

final class Name extends IdeT with L[Nothing, Val[Name]]

sealed trait SignatoryVal extends IdeT with L[Nothing, Val[SignatoryVal]]
final class  SignatoryKey extends SignatoryVal

sealed trait LocVal[+T <: A, +C <: Loc[T]] extends L[Nothing, C]
final class  LocKey[+T <: A] extends IdeT with LocVal[T, Loc[T]] // Allows broadcasting
// Could LocVals be dependently typed, to bind them to a scope?

final case class Broadcast[+K <: N, +T <: A](address: L[K, Val[LocKey[T]]]) extends L[K, Loc[T]]

final case class Start[+K <: N](name: L[K, Val[Name]]) extends L[K, Loc[U]]
final case class Pause[+K <: N](name: L[K, Val[Name]]) extends L[K, Loc[U]]
final case class Dissolve[+K <: N](name: L[K, Val[Name]]) extends L[K, Loc[U]]
final case class Kill[+K <: N](name: L[K, Val[Name]]) extends L[K, Loc[U]]
final case class Move[+K <: N](name: L[K, Val[Name]]) extends L[K, Loc[Name]]
