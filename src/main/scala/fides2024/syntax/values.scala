package fides2024.syntax

object Unit extends Val
object True extends Val
object False extends Val
object Nil extends Val

final case class Pair(first: Val, second: Val) extends Val
final case class QuoteVal(code: Component) extends Val
final case class Escape[P <: Polarity](code: Expr[P]) extends Val

sealed class Identifier extends Val derives CanEqual
final class Address extends Identifier
final class IdentifierKey extends Val:
  val identifier: Identifier = new Identifier
end IdentifierKey

final case class Signed private(message: Val, signature: Identifier) extends Val
object Signed:
  def apply(message: Val, key: IdentifierKey): Signed =
    new Signed(message, key.identifier)
end Signed
