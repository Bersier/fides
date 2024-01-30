package fides.syntax.identifiers

import fides.syntax.code.{Code, Expr, ValQ, ValType}
import izumi.reflect.Tag

/**
  * A key has a corresponding identifier. The identifer can be obtained from the key, but not vice versa
  * (there might not even exist a corresponding key for some identifiers).
  */
sealed class IdentifierKey(name: String)(using Context) extends ValQ[IdentifierKey], ValType:
  lazy val identifier: Identifier = Identifier(name)
  override def toString: String = s"Key(${identifier.name})"
object IdentifierKey:
  def apply()(using Context): IdentifierKey = new IdentifierKey(Identifier.newName())
end IdentifierKey

final class ChannelKey[T <: ValType : Tag](name: String)(using Context) extends IdentifierKey(""):
  override lazy val identifier: Channel[T] = Channel(name)
object ChannelKey:
  def apply[T <: ValType : Tag]()(using Context): ChannelKey[T] = new ChannelKey(Identifier.newName())
end ChannelKey

/**
  * Outputs the identifier corresponding to the inputted key.
  *
  * Equivalent to signing a dummy message, and then extracting the signature from it via pattern matching.
  */
final case class ExtractIdentifier(key: Code[Expr[IdentifierKey]]) extends Expr[Identifier]
