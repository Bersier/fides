package fides2024.syntax.identifiers

import fides2024.syntax.kinds.{Code, Expr, Val, ValType}

/**
  * A key has a corresponding identifier. The identifer can be obtained from the key, but not vice versa
  * (there might not even exist a corresponding key for some identifiers).
  */
sealed class IdentifierKey(using Context) extends Val[IdentifierKey]:
  val identifier: Identifier = Identifier()
  override def toString: String = s"Key(${identifier.name})"
end IdentifierKey

final class ChannelKey[T <: ValType](using Context) extends IdentifierKey:
  override val identifier: Channel[T] = Channel()
end ChannelKey

/**
  * Outputs the identifier corresponding to the inputted key.
  *
  * Equivalent to signing a dummy message, and then extracting the signature from it via pattern matching.
  */
final case class ExtractIdentifier(key: Code[Expr[IdentifierKey]]) extends Expr[Identifier]
