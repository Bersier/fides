package fides.syntax.identifiers

import fides.syntax.types.*

/**
  * Identifiers are structureless. They can only be compared for equality. They cannot be inspected in any other way.
  * New identifiers can be created. Identifiers are globally unique.
  *
  * Implementation note: Due to polarity in Fides, type widening is unsound.
  * So every [[Ntrl]] should be singleton-typed. That is why [[Identifier]] takes an [[ID]].
  * This way, its code type can be unique to that particular identifier.
  * Additional access restrictions are there to enforce type tightness.
  */
final case class Identifier[K <: ID] private(private val k: K) extends Code[Ntrl[IdentifierT[K]]]
object Identifier:
  def apply(): Identifier[?] = new Identifier(ID())
  def apply[K <: ID & Singleton](k: K): Identifier[K] = new Identifier(k)
end Identifier

/**
  * A key has a corresponding identifier. The identifier can be obtained from the key, but not vice versa
  * (there might not even exist a corresponding key for some identifiers).
  */
final case class IdentifierKey[K <: ID] private(private val k: K) extends Code[Ntrl[IdentifierKeyT[K]]]
object IdentifierKey:
  def apply(): IdentifierKey[?] = new IdentifierKey(ID())
  def apply[K <: ID & Singleton](k: K): IdentifierKey[K] = new IdentifierKey(k)
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
final case class ExtractIdentifier[K <: ID](
  key: Code[Expr[IdentifierKeyT[K]]],
) extends Code[Exvr[IdentifierT[K]]]
