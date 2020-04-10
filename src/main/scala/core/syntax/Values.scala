package core.syntax

final class Name extends Val

sealed trait Signatory extends Val
final class SignatoryKey() extends Signatory {
  def signatory: Signatory = this
}

sealed trait Error extends Val