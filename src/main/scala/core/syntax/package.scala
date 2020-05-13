package core

package object syntax {
  type Multiset[A] = Map[A, BigInt]

  sealed trait Sort { type R; type Q = R }
  sealed trait Prs extends Sort { override type R = Prs }
  sealed trait Exp extends Sort { override type R = Exp }
  sealed trait Loc extends Exp { override type R = Exp }
  sealed trait Key extends Exp { override type R = Exp }
  sealed trait Val extends Exp { override type R = Val; override type Q = Exp }

  sealed trait Dir[_]
  sealed trait Inp[+T <: A] extends Dir[_]
  sealed trait Out[-T <: A] extends Dir[_]
  type Both[T <: A] = Inp[T] with Out[T]

  sealed trait N
  sealed trait RegularK extends N
  sealed trait CodeK[+K <: N, +S <: Sort, +D[_ <: A] <: Dir[_]] extends N

  trait A
  trait P extends A
  trait E extends A
//  trait Ide extends A

//  sealed trait Ide extends Eva { override type R = Eva }
//  sealed trait Lid extends Loc with Ide
//  sealed trait Vid extends Val with Ide
//  sealed trait Key extends Lid

  trait L[+K <: N, +S <: Sort, +D <: Dir[_]]

  type V[T <: A] = L[Nothing, Val, Both[T]]

  val Launcher: SignatoryVal = new SignatoryKey
  val ErrorLoc: L[Nothing, Loc, Out[A]] = new LocKey

  // (Multi?)Sets (and Seqs, and Ints)
  // Shell
  // Polishing
  // Test
  // Semantics
  // Concrete syntax?
  // Share?
}
