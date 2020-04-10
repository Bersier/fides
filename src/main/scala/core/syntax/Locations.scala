package core.syntax

trait Loc[+K <: N, +C <: D, +T <: X[K, C, T]] extends X[K, C, T]

sealed trait Address extends Loc[AllK, Inp with Out, Nothing]
object Address {
  def newOne(): Address = Key.newOne()
}

/**
  * Keys allow broadcasting.
  */
sealed trait Key extends Address
object Key {
  def newOne(): Key = new Key{}
}

final case class Broadcast(address: Key) extends O[AllK, Nothing]

sealed trait Command[+T <: V[T]] extends O[AllK, T]
final class Start(name: Name) extends Command[U]
final class Pause(name: Name) extends Command[U]
final class Dissolve(name: Name) extends Command[U]
final class Kill(name: Name) extends Command[U]
final class Move(name: Name) extends Command[Name]
