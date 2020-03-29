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
  final case class Copy(inLoc: InLoc, outLocs: Multiset[OutLoc])

  final case class Wait(token: InLoc, inLoc: InLoc, outLoc: OutLoc) extends Primitive

  final case class Join(first: InLoc, second: InLoc, pair: OutLoc) extends Primitive
  final case class Split(pair: InLoc, first: OutLoc, second: OutLoc) extends Primitive
  
  final case class Concurrent(processes: Multiset[Process]) extends Process
  final case class Replicated(broadcast: Set[InLoc], process: Process) extends Process // Added broadcast param here
  // I'm thinking about how it's not possible to communicate to a replicated process otherwise... but do we really
  // want that?

  final case class New(addresses: Set[Loc], process: Process)
  
  final case class Awake(name: Name, process: Process) extends Process
  final case class Asleep(name: Name, process: Process) extends Process

  final case class Swappable(location: InLoc, process: Process) extends Process
}
