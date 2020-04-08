package core.syntax

trait Loc[+K <: N, +T <: V[K], C <: D] extends X[K, C]
sealed trait Address[K <: N] extends Inp[K, Nothing] with Out[K, Nothing]
object Address {
  def newOne(): Address[AllK] = Key.newOne()
}

sealed trait Key[K <: N] extends Address[K]// Key allows broadcasting
object Key {
  def newOne(): Key[AllK] = new Key[AllK]{}
}

final case class Broadcast[K <: N, T <: V[K]](address: Key[K]) extends Out[K, T]

sealed trait Command[T <: Val] extends Out[AllK, T]
final class Start(name: Name) extends Command[U]
final class Pause(name: Name) extends Command[U]
final class Dissolve(name: Name) extends Command[U]
final class Kill(name: Name) extends Command[U]
final class Move(name: Name) extends Command[Destination]