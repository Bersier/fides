package fides.syntax.identifiers

import fides.syntax.core.Code
import fides.syntax.types.{Expr, Exvr, IdentifierID, IdentifierUT, IdentifierKeyUT, IdentifierKeyT, IdentifierT, Ntrl}

/**
  * Identifiers are structureless. They can only be compared for equality. They cannot be inspected in any other way.
  * New identifiers can be created. Identifiers are globally unique.
  *
  * Implementation note: Due to polarity in Fides, type widening is unsound.
  * So every [[Ntrl]] should be singleton-typed. That is why [[Identifier]] takes an [[IdentifierID]].
  * This way, its code type can be unique to that particular identifier.
  * Additional access restrictions are there to enforce type tightness.
  */
final case class Identifier[K <: IdentifierID] private(private val k: K) extends Code[Ntrl[IdentifierT[K]]]
object Identifier:
  def apply(): Identifier[?] = new Identifier(IdentifierID())
  def apply[K <: IdentifierID & Singleton](k: K): Identifier[K] = new Identifier(k)
end Identifier

/**
  * A key has a corresponding identifier. The identifier can be obtained from the key, but not vice versa
  * (there might not even exist a corresponding key for some identifiers).
  */
final case class IdentifierKey[K <: IdentifierID] private(private val k: K) extends Code[Ntrl[IdentifierKeyT[K]]]
object IdentifierKey:
  def apply(): IdentifierKey[?] = new IdentifierKey(IdentifierID())
  def apply[K <: IdentifierID & Singleton](k: K): IdentifierKey[K] = new IdentifierKey(k)
end IdentifierKey

/**
  * Outputs a new identifier.
  */
final case class NewIdentifier() extends Code[Exvr[IdentifierUT]]

/**
  * Outputs a new identifier key.
  */
final case class NewIdentifierKey() extends Code[Exvr[IdentifierKeyUT]]

/**
  * Outputs the identifier corresponding to the inputted key.
  *
  * Equivalent to signing a dummy message, and then extracting the signature from it via pattern matching.
  */
final case class ExtractIdentifier[K <: IdentifierID](
  key: Code[Expr[IdentifierKeyT[K]]],
) extends Code[Exvr[IdentifierT[K]]]
