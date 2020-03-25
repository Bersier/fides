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

  final case class Forward(inLoc: InLoc, outLoc: OutLoc) extends Primitive

  /** Can only be used to broadcast to locs in this code, not external ones **/
  // Forgot why we need this again. Write down example, next time.
//  final case class Broadcast(inLoc: InLoc, outLoc: OutLoc) extends Primitive

  final case class Wait(token: InLoc, inLoc: InLoc, outLoc: OutLoc) extends Primitive

  final case class Join(first: InLoc, second: InLoc, pair: OutLoc) extends Primitive
  final case class Split(pair: InLoc, first: OutLoc, second: OutLoc) extends Primitive

  final case class Parallel(processes: Multiset[Process]) extends Process
  final case class Replicated(addresses: Set[Loc], process: Process) extends Process
  // Semantics?

  // final case class Scope? // to specify loc privacy

  final case class Awake(command: InLoc, process: Process) extends Process
  final case class Asleep(command: InLoc, process: Process) extends Process

  final case class Branch(boolean: InLoc, trueBranch: Process, falseBranch: Process) extends Process

  // Swappable(process, address)
  // Swap(process: InLoc, at: InLoc |?| Address)

/*
How to get a natural calculus for  Sleep, Kill, ...?

Scenarios
 - Internal choice on receive
 - 

Ambient calculus?
*/

  // Swap

  /**
    * For convenience, so that we don't need to reimplement basics.
    */
  final case class Execute[A, B](input: InLoc, function: A => B, output: OutLoc) extends Process
}
