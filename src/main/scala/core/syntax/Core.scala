package core.syntax

final case class Constant[T <: Val](value: T) extends Loc[T]

sealed trait Pattern
sealed trait Process extends Pattern

sealed trait Primitive extends Process
final case class Copy[T <: Val](inLoc: InLoc[T], outLocs: Multiset[OutLoc[T]]) extends Primitive
final case class Wait[T <: Val](token: InLoc[Unit.type], inLoc: InLoc[T], outLoc: OutLoc[T]) extends Primitive
final case class Join[S <: Val, T <: Val](one: InLoc[S], two: InLoc[T], pair: OutLoc[APair[S, T]]) extends Primitive
final case class Split[S <: Val, T <: Val](pair: InLoc[APair[S, T]], one: OutLoc[S], two: OutLoc[T]) extends Primitive

final case class Concurrent(processes: Multiset[Process]) extends Process
final case class Replicated(process: Process) extends Process
final case class New(addresses: Set[Loc[_]], process: Process)
final case class Awake(name: Name, process: Process) extends Process
final case class Asleep(name: Name, process: Process) extends Process
final case class Swappable(inLoc: InLoc[Code], process: Process) extends Process
final case class Annotated(process: Process, annotation: Process) extends Process
final case class Guarded(handler: OutLoc[Error], process: Process) extends Process
final case class Shell() extends Process {
  def send(value: Val): Unit = ???
  def register[T <: Val](inLoc: InLoc[T])(consumer: T => ()): Unit = ???
  def registerReplicated[T <: Val](inLoc: InLoc[T])(consumer: T => ()): Unit = ???
  def remove() = ???
}

final case class Var(outLoc: OutLoc[Code]) extends Pattern

trait Loc[+T <: Val]
object Loc {
  def apply[T <: Val](): Loc[T] = new PrivateAddress[T]
}