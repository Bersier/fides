package core

package object syntax {
  type Multiset[A] = Map[A, BigInt]

  sealed trait D
  sealed trait Pro extends D
  sealed trait Exp[+T <: ValT] extends D
  sealed trait Inp[+T <: ValT] extends Exp[T]
  sealed trait Out[+T <: ValT] extends Exp[T]
  sealed trait Val[+T <: ValT] extends Inp[T] with Out[T]
  sealed trait Loc[+T <: ValT]  extends Inp[T] with Out[T]

  sealed trait N
  sealed trait RegularK extends N
  sealed trait CodeK[+K <: N, +C <: D] extends N

  trait ValT
  sealed trait LocVal[+T <: ValT] extends ValT
  
  trait L[+K <: N, +C <: D]
  
  // Sets
  // Shell
  // Polishing
  // Test
  // Semantics
  // Concrete syntax?
  // Share?
}
