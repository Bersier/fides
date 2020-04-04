package core.syntax

trait Loc[+T <: Val] extends Val
object Loc {
  def apply[T <: Val](): Loc[T] = new Key
}
trait InLoc[+T <: Val] extends Loc[T]
trait OutLoc[+T <: Val] extends Loc[T]
sealed class Address extends InLoc[Nothing] with OutLoc[Nothing]

final class Key extends Address {
  def address: Address = this
}
final case class Broadcast[T <: Val](address: Key) extends OutLoc[T]

sealed trait Command[T <: Val] extends OutLoc[T]
final class Start(name: Name) extends Command[Unit.type]
final class Pause(name: Name) extends Command[Unit.type]
final class Dissolve(name: Name) extends Command[Unit.type]
final class Kill(name: Name) extends Command[Unit.type]
final class Move(name: Name) extends Command[Destination]