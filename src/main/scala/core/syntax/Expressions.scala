package core.syntax

trait E[+K <: I, +S <: G] extends Lex[K]

final case class APair[K <: I, S <: G, T1 <: E[K, S], T2 <: E[K, S]](first: T1, second: T2) extends E[K, S]

final case class Code[K <: I, S <: G](process: P[K]) extends V[K] // Do I now need to feed S to P as well?! Also, does having S == A guarantee that the process is well-built? Not sure...
final case class Patt(pattern: Pattern) extends Val // todo...

final case class Signed[K <: I, T <: V[K]] private(contents: T, signatory: Signatory) extends V[K] {
  def this(message: T, signatoryKey: SignatoryKey) = this(message, signatoryKey.signatory)
}
object Signed {
  def pattern[T <: V[PatternK]](contents: T, signatory: Signatory): Signed[PatternK, T] = {
    new Signed(contents, signatory)
  }
}

final case class Var[K <: I, T <: V[K]](group: Address[K], outLoc: Out[K, T]) extends
  E[K, G] with
  P[PatternK] with
  Loc[PatternK, Nothing] // with ...
