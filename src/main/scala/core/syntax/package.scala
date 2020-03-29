package core

package object syntax {

  type Multiset[A] = Map[A, BigInt]

  type OutLoc[T <: Val] = Loc[T]
  type InLoc[T <: Val] = Loc[T]
}
