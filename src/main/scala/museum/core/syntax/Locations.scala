package museum.core.syntax

final class Name extends IdeT with L[Nothing, Val[Name]]
// Maybe merge some ide-like sorts

sealed trait SignatoryVal extends IdeT with L[Nothing, Val[SignatoryVal]]
final class  SignatoryKey extends SignatoryVal

sealed trait LocVal[T <: A, +C >: Inp[T] with Out[T] <: Out[T]] extends LocT with L[Nothing, C]
final class  LocKey[T <: A] extends IdeT with LocVal[T, Out[T]] // Allows broadcasting
// Could LocVals be dependently typed, to bind them to a scope?

final case class Broadcast[+K <: N, T <: A](address: L[K, Val[LocKey[T]]]) extends L[K, Out[T]]

final case class Start[+K <: N](name: L[K, Val[Name]]) extends LocT with L[K, Out[U]]
final case class Pause[+K <: N](name: L[K, Val[Name]]) extends LocT with L[K, Out[U]]
final case class Dissolve[+K <: N](name: L[K, Val[Name]]) extends LocT with L[K, Out[U]]
final case class Kill[+K <: N](name: L[K, Val[Name]]) extends LocT with L[K, Out[U]]
final case class Move[+K <: N](name: L[K, Val[Name]]) extends LocT with L[K, Out[Name]]
