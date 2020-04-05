package core.syntax

trait V[+K <: I] extends Lex[K]
final class Name extends Val

final class Destination(name: Name) extends Val

final case class APair[K <: I, S <: V[K], T <: V[K]](first: S, second: T) extends V[K]

final case class Code[K <: I](process: Proc[K]) extends V[K] // can we not explain the sprawling values as processes?
final case class Patt(pattern: Pattern) extends Val

final case class Signed[K <: I, T <: V[K]] private(contents: T, signatory: Signatory) extends V[K] {
  def this(message: T, signatoryKey: SignatoryKey) = this(message, signatoryKey.signatory)
}
object Signed {
  def pattern[T <: V[PatternK]](contents: T, signatory: Signatory): Signed[PatternK, T] = {
    new Signed(contents, signatory)
  }
}

sealed trait Signatory extends Val
final class SignatoryKey() extends Signatory {
  def signatory: Signatory = this
}

sealed trait Error extends Val