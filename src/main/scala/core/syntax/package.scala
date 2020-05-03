package core

package object syntax {
  type Multiset[A] = Map[A, BigInt]

  sealed trait D // Sort
  sealed trait Pro[_] extends D // Type argument only needed for uniformity
  sealed trait Exp extends D
  sealed trait Inp[+T <: A] extends Exp
  sealed trait Out[-T <: A] extends Exp
  sealed trait Loc[-T <: A] extends Out[T]
  sealed trait Val[T <: A] extends Inp[T] with Out[T]

  type X = Exp
  type Prs = Pro[A]

  sealed trait N
  sealed trait RegularK extends N
  sealed trait CodeK[+K <: N, +C[_ <: A] <: X] extends N

  trait A
  trait IdeT extends A

  trait L[+K <: N, +C <: D]

  type V[T <: A] = L[Nothing, Val[T]]

  val Launcher: SignatoryVal = new SignatoryKey
  val ErrorLoc: L[Nothing, Loc[A]] = new LocKey

  // (Multi?)Sets (and Seqs, and Ints)
  // Shell
  // Polishing
  // Test
  // Semantics
  // Concrete syntax?
  // Share?
}
