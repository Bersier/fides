package fides2024.syntax

object Unit extends Val
object True extends Val
object False extends Val
object Nil extends Val

final case class Pair[FirstT <: ValSup, SecondT <: ValSup]
(first: FirstT, second: SecondT) extends Val[Pair[FirstT, SecondT]]
final case class QuoteVal(code: Component) extends Val[QuoteVal]
final case class Escape[P <: Polarity](code: Expr[P, ValSup]) extends Val[Nothing]

sealed class Identifier extends Val[Identifier] derives CanEqual
final class IdentifierKey extends Val[IdentifierKey]:
  val identifier: Identifier = new Identifier
end IdentifierKey

final case class Signed[ContentsT <: ValSup] private
(message: Val[ContentsT], signature: Identifier) extends Val[Signed[ContentsT]]
object Signed:
  def apply[ContentsT <: ValSup](message: Val[ContentsT], key: IdentifierKey): Signed[ContentsT] =
    new Signed(message, key.identifier)
end Signed
