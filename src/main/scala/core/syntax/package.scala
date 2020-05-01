package core

package object syntax {
  type Multiset[A] = Map[A, BigInt]

  sealed trait D // Sort
  sealed trait Prs extends D
  sealed trait Exp[+T <: ValT] extends D
  sealed trait Inp[+T <: ValT] extends Exp[T]
  sealed trait Out[+T <: ValT] extends Exp[T]
  sealed trait Loc[+T <: ValT] extends Out[T]
  sealed trait Val[+T <: ValT] extends Inp[T] with Out[T]
  sealed trait Ide[+T <: ValT] extends Inp[T] with Out[T]

  type Vid[+T <: ValT] = Val[T] with Ide[T]
  type Adr[+T <: ValT] = Loc[T] with Ide[T]

  sealed trait N
  sealed trait RegularK extends N
  sealed trait CodeK[+K <: CodeK[_, _], +C[+_] <: D] extends N

  trait ValT

  trait L[+K <: N, +C <: D]

  type V[+T <: ValT] = ValT with L[Nothing, Val[T]]

  // (Multi?)Sets (and Seqs, and Ints)
  // Shell
  // Polishing
  // Test
  // Semantics
  // Concrete syntax?
  // Share?
}
