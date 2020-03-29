package object syntax {

  type Multiset[A] = Map[A, BigInt]

  sealed trait Loc
  type OutLoc = Loc
  type InLoc = Loc
}
