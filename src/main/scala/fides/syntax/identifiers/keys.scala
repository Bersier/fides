package fides.syntax.identifiers

import fides.syntax.core.Code
import fides.syntax.types.{Expr, Exvr, IdentifierKeyT, IdentifierT, Ntrl}

/**
  * A key has a corresponding identifier. The identifier can be obtained from the key, but not vice versa
  * (there might not even exist a corresponding key for some identifiers).
  */
sealed class IdentifierKey extends Code[Ntrl[IdentifierKeyT]]

//final class ChannelKey[T <: ValTop : Tag](name: String)(using Context) extends IdentifierKey(""):
//  override lazy val identifier: OutChan[T] = Channel(name)
//object ChannelKey:
//  def apply[T <: ValTop : Tag]()(using Context): ChannelKey[T] = new ChannelKey(Named.newName())
//end ChannelKey

/**
  * Outputs the identifier corresponding to the inputted key.
  *
  * Equivalent to signing a dummy message, and then extracting the signature from it via pattern matching.
  */
final case class ExtractIdentifier(key: Code[Expr[IdentifierKeyT]]) extends Code[Exvr[IdentifierT]]
