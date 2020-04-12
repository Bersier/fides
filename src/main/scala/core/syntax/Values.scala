package core.syntax

final case class U() extends V[Nothing, U]

sealed trait BoolVal extends V[Nothing, BoolVal]
object True extends BoolVal
object False extends BoolVal

sealed trait Error extends V[Nothing, Error]

final class Name extends V[Nothing, Name]

sealed trait Signatory extends V[Nothing, Signatory]
final class SignatoryKey() extends Signatory
