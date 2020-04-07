package core.syntax

trait E[+K <: I] extends Lex[K]

final case class APair[K <: I, T1 <: E[K], T2 <: E[K]](first: T1, second: T2) extends E[K]

// Do I now need to feed S to P as well?!
// Also, does having S == A guarantee that the process is well-built? Not sure...
final case class Code[S <: G](process: Lex[CodeK[S]]) extends E[AllK[S]]
final case class Patt[S <: G](pattern: Lex[PattK[S]]) extends E[AllK[S]]

final case class Escape(expr: E[RegularK[_]]) extends E[CodeK[G]]

/**
  * Matches Escape when level is zero. Otherwise, matches Epacse of a level lower by one.
  */
final case class Epacse[S <: G](level: BigInt, expr: E[PattK[S]]) extends E[PattK[S]]

final case class Signed[+K <: I, T <: E[K]] private(contents: T, signatory: Signatory) extends E[K] {
  def this(message: T, signatoryKey: SignatoryKey) = this(message, signatoryKey.signatory)
}
object Signed {
  def pattern[S <: G, T <: E[PattK[S]]](contents: T, signatory: Signatory): Signed[PattK[S], T] = {
    new Signed(contents, signatory)
  }
}

// Need dual of this as well, right? (Can use inherited abstract class for implementation)
final case class Var[K <: I, T <: V[K]](group: Address[K], outLoc: Out[K, T]) extends
  E[K] with
  P[PattK] with
  Loc[PattK, Nothing] // with ...
