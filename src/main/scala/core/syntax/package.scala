package core

package object syntax {
  type Multiset[A] = Map[A, BigInt]

  sealed trait Dir
  sealed trait Inp extends Dir
  sealed trait Out extends Dir

  sealed trait D // Sort
  sealed trait Prs extends D
  sealed trait Exp[+T <: ValT, +R <: Dir] extends D
  sealed trait Loc[+T <: ValT, +R <: Out] extends D
  sealed trait Ide[+T <: ValT] extends D

  type Val[+T <: ValT] = Exp[T, Nothing]
  type Vid[+T <: ValT] = Val[T] with Ide[T]
  type Adr[+T <: ValT] = Loc[T, Nothing] with Ide[T]

  sealed trait N
  sealed trait RegularK extends N
  sealed trait CodeK[+K <: CodeK[_, _], +C[+_] <: D] extends N

  trait ValT
  
  trait L[+K <: N, +C <: D]

  type V[+T <: ValT] = ValT with L[Nothing, Val[T]]
  
  // Sets
  // Shell
  // Polishing
  // Test
  // Semantics
  // Concrete syntax?
  // Share?
}
