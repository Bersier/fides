package fides2024.syntax

// todo make Val[T] equivalent to Quotation[Val[T]]?

object Unit extends Val[Unit.type]
sealed trait Bool extends Val[Bool]
object True extends Val[Bool]
object False extends Val[Bool]

final case class Pair[T1 <: ValType, T2 <: ValType]
(first: Code[Val[T1]], second: Code[Val[T2]]) extends Val[Pair[T1, T2]]

sealed trait Collection[T <: ValType] extends Val[Collection[T]]
object Empty extends Collection[Nothing]
final case class NonEmpty[T <: ValType](elements: Iterable[T]) extends Collection[T]:
  assert(elements.nonEmpty)
end NonEmpty

/**
  * Code as value, used for metaprogramming.
  */
final case class Quotation[C <: CodeType](code: Code[C]) extends Val[Quotation[C]]

/**
  * Identifiers are structureless. They can only be compared for equality. New identifiers can be created.
  * It is not possible to construct an identifier in any other way.
  */
final class Identifier extends Val[Identifier] derives CanEqual

/**
  * A key has a corresponding identifier. The identifer can be obtained from the key, but not vice versa
  * (there might not even exist a corresponding key for some identifiers).
  */
final class IdentifierKey extends Val[IdentifierKey]:
  val identifier: Identifier = new Identifier
end IdentifierKey

/**
  * @param document the signed value
  * @param signature the identifier corresponding to the key that was used to sign the document
  * @tparam T the type of the signed value
  */
final case class Signed[T <: ValType] private(document: Val[T], signature: Identifier) extends Val[Signed[T]]
object Signed:
  /**
    * Signed values can only be created from keys, but only reveal the corresponding identifier.
    */
  def apply[T <: ValType](document: Val[T], signatory: IdentifierKey): Signed[T] =
    new Signed(document, signatory.identifier)
end Signed

/**
  * Since Signed values cannot be created freely, a different one is needed for matching. To match the latter, yet
  * a different one is needed, and so forth. This is solved by having a level, effectively introducing a hierarchy of
  * matchers.
  *
  * SignedMatcher(1, m, s) matches Signed(m, s).
  * For level > 1, SignedMatcher(level, m, s) matches SignedMatcher(level - 1, m, s).
  */
final case class SignedMatcher[T <: ValType]
(level: BigInt, document: Code[Val[T]], signature: Code[Val[Identifier]]) extends Val[Signed[T]]:
  assert(level > 0)
end SignedMatcher
// todo should only be allowed in code patterns (although maybe it's not such a big deal if it can be used elsewhere)
