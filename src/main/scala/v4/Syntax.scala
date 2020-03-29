package v4

/**
  *
  */
object Syntax {
  type Multiset[A] = Map[A, BigInt]

  sealed trait Loc // Loc[Val] ?
  type OutLoc = Loc
  type InLoc = Loc

  sealed trait Process
  sealed trait Primitive extends Process

  // final case class Code(process: Process)

  final case class Constant(value: Val, outLoc: OutLoc) extends Primitive

  final case class Copy(inLoc: InLoc, outLocs: Multiset[OutLoc])
  // For quantum or linear values, we would use something more restricted, like (late) Forward.

  final case class Wait(token: InLoc, inLoc: InLoc, outLoc: OutLoc) extends Primitive

  final case class Join(first: InLoc, second: InLoc, pair: OutLoc) extends Primitive
  final case class Split(pair: InLoc, first: OutLoc, second: OutLoc) extends Primitive
  
  final case class Concurrent(processes: Multiset[Process]) extends Process
  final case class Replicated(process: Process) extends Process

  final case class New(addresses: Set[Loc], process: Process)
  
  final case class Awake(name: Name, process: Process) extends Process
  final case class Asleep(name: Name, process: Process) extends Process
  // Should asleep processes still be able to receive? No, right?

  final case class Swappable(location: InLoc, process: Process) extends Process
}
