package core.syntax

sealed trait Process

sealed trait Primitive extends Process
final case class Constant(value: Val, outLoc: OutLoc) extends Primitive
final case class Copy(inLoc: InLoc, outLocs: Multiset[OutLoc]) extends Primitive
final case class Wait(token: InLoc, inLoc: InLoc, outLoc: OutLoc) extends Primitive
final case class Join(first: InLoc, second: InLoc, pair: OutLoc) extends Primitive
final case class Split(pair: InLoc, first: OutLoc, second: OutLoc) extends Primitive

final case class Concurrent(processes: Multiset[Process]) extends Process
final case class Replicated(process: Process) extends Process
final case class New(addresses: Set[Loc], process: Process)
final case class Awake(name: Name, process: Process) extends Process
final case class Asleep(name: Name, process: Process) extends Process
final case class Swappable(inLoc: InLoc, process: Process) extends Process
final case class Annotated(process: Process, annotation: Process) extends Process