package fides.syntax.identifiers

import fides.syntax.code.{Code, Expr, Lit, ValTop}
import fides.syntax.identifiers.naming.{Context, Named}
import izumi.reflect.Tag

/**
  * A key has a corresponding identifier. The identifier can be obtained from the key, but not vice versa
  * (there might not even exist a corresponding key for some identifiers).
  */
sealed class IdentifierKey(name: String)(using Context) extends Lit[IdentifierKey], ValTop:
  lazy val identifier: Identifier = Identifier(name)
  override def toString: String = s"Key(${identifier.name})"
object IdentifierKey:
  def apply()(using Context): IdentifierKey = new IdentifierKey(Named.newName())
end IdentifierKey

final class ChannelKey[T <: ValTop : Tag](name: String)(using Context) extends IdentifierKey(""):
  override lazy val identifier: OutChan[T] = Channel(name)
object ChannelKey:
  def apply[T <: ValTop : Tag]()(using Context): ChannelKey[T] = new ChannelKey(Named.newName())
end ChannelKey

/**
  * Outputs the identifier corresponding to the inputted key.
  *
  * Equivalent to signing a dummy message, and then extracting the signature from it via pattern matching.
  */
final case class ExtractIdentifier(key: Code[Expr[IdentifierKey]]) extends Expr[Identifier]
