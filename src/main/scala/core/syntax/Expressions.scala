package core.syntax

trait E[+K <: I] extends Lex[K]

final case class APair[K <: I, T1 <: E[K], T2 <: E[K]](first: T1, second: T2) extends E[K]

// Do I now need to feed S to P as well?!
// Also, does having S == A guarantee that the process is well-built? Not sure...
// Should be able to hold other stuff than P as well.
// Also, should be able to hold unevaluated expressions while itself being a value.
// Need something to play the role of #. Only inside Code (and co)? Or everywhere?
// Why not allow naked P as expr?
final case class Code[K <: I](process: P[K]) extends E[K]
final case class Patt[S <: G](pattern: Pattern[S]) extends E[BothK[S]]
// Does pattern inside pattern work?

final case class Signed[+K <: I, T <: E[K]] private(contents: T, signatory: Signatory) extends E[K] {
  def this(message: T, signatoryKey: SignatoryKey) = this(message, signatoryKey.signatory)
}
object Signed {
  def pattern[S <: G, T <: E[PatternK[S]]](contents: T, signatory: Signatory): Signed[PatternK[S], T] = {
    new Signed(contents, signatory)
  }
}

// Need dual of this as well, right? (Can use inherited abstract class for implementation)
final case class Var[K <: I, T <: V[K]](group: Address[K], outLoc: Out[K, T]) extends
  E[K] with
  P[PatternK] with
  Loc[PatternK, Nothing] // with ...
