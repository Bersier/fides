package core.syntax

trait Loc[+K <: I, +T <: V[K]] extends V[K]
object Loc {
  def newOne[T <: Val](): Loc[AllK, T] = Address.newOne()
}
trait Inp[+K <: I, +T <: V[K]] extends Loc[K, T] with E[K]
trait Out[+K <: I, +T <: V[K]] extends Loc[K, T] with R[K]
sealed trait Address[K <: I] extends Inp[K, Nothing] with Out[K, Nothing]
object Address {
  def newOne(): Address[AllK] = Key.newOne()
}

sealed trait Key[K <: I] extends Address[K]// Key allows broadcasting
object Key {
  def newOne(): Key[AllK] = new Key[AllK]{}
}

final case class Broadcast[K <: I, T <: V[K]](address: Key[K]) extends Out[K, T]

sealed trait Command[T <: Val] extends Out[AllK, T]
final class Start(name: Name) extends Command[U]
final class Pause(name: Name) extends Command[U]
final class Dissolve(name: Name) extends Command[U]
final class Kill(name: Name) extends Command[U]
final class Move(name: Name) extends Command[Destination]