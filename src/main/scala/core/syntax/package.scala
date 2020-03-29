package core

package object syntax {
  type Multiset[A] = Map[A, BigInt]

  type OutLoc[T <: Val] = Loc[T]
  type InLoc[T <: Val] = Loc[T]

  val Mailer     = new Address[Nothing]
  val Comparator = new Address[Nothing]
  val KeySmith   = new Address[Nothing]
  val Witness    = new Address[Nothing]
  val Notary     = new Address[Nothing]
  val Compiler   = new Address[Nothing]
}
