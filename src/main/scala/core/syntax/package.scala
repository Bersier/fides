package core

package object syntax {
  type Multiset[A] = Map[A, BigInt]

  sealed trait Sort // Couldn't the R type be added to sorts?
  sealed trait D[_] extends Sort
  sealed trait Pro[_] extends Sort // Type argument only needed for uniformity
  sealed trait Exp extends Sort
  sealed trait Inp[+T <: A] extends Exp
  sealed trait Out[-T <: A] extends Exp

  type X = Exp
  type Prs = Pro[A]
  type G = Eva

  sealed trait N
  sealed trait RegularK extends N
  sealed trait CodeK[+K <: N, +C[_ <: A] <: X, +B <: Eva] extends N

  trait A

  sealed trait Prc { type R = Prc } //
  sealed trait Eva extends Prc { override type R = Eva }
  sealed trait Loc extends Eva { override type R = Eva }
  sealed trait Ide extends Eva { override type R = Eva }
  sealed trait Val extends Eva { override type R = Val }
  sealed trait Lid extends Loc with Ide
  sealed trait Vid extends Val with Ide
  sealed trait Key extends Lid

  trait M[+K <: N, +C <: Sort, +B <: Eva]
  type L[+K <: N, +C <: Sort] = M[K, C, Eva]

  type V[T <: A] = M[Nothing, Inp[T] with Out[T], Val]

  val Launcher: SignatoryVal = new SignatoryKey
  val ErrorLoc: M[Nothing, Out[A], Loc] = new LocKey

  // (Multi?)Sets (and Seqs, and Ints)
  // Shell
  // Polishing
  // Test
  // Semantics
  // Concrete syntax?
  // Share?
}
