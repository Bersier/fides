package core.syntax

trait Val
final class Name extends Val

final class Destination(name: Name) extends Val

final case class APair[S <: Val, T <: Val](first: S, second: T) extends Val

final case class Code(pattern: Pattern) extends Val // can we not explain the sprawling values as processes?
final case class Patt(pattern: Pattern) extends Val

final case class Signed[T <: Val] private(contents: T, signatory: Signatory) extends Val {
  def this(message: T, signatoryKey: SignatoryKey) = this(message, signatoryKey.signatory)
}

sealed trait Signatory extends Val
final class SignatoryKey() extends Signatory {
  def signatory: Signatory = this
}

sealed trait Error extends Val