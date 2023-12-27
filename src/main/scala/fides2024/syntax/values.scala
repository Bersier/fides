package fides2024.syntax

object Unit extends Val[Unit.type]
sealed trait Bool extends Val[Bool]
object True extends Val[Bool]
object False extends Val[Bool]

final case class Pair[FirstT <: ValType, SecondT <: ValType]
(first: Code[Val[FirstT]], second: Code[Val[SecondT]]) extends Val[Pair[FirstT, SecondT]]

// sealed trait Collection[ElementT <: F[K]] extends F[Collection[K]]
sealed trait Collection[ElementT <: ValType] extends Val[Collection[ElementT]]
object Empty extends Collection[Nothing]
final case class NonEmpty[ElementT <: ValType](elements: Iterable[ElementT]) extends Collection[ElementT]:
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
sealed class Identifier extends Val[Identifier] derives CanEqual

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
  * @tparam ContentsT the type of the signed value
  */
final case class Signed[ContentsT <: ValType] private
(document: Val[ContentsT], signature: Identifier) extends Val[Signed[ContentsT]]
object Signed:
  /**
    * Signed values can only be created from keys, but only reveal the corresponding identifier.
    */
  def apply[ContentsT <: ValType](document: Val[ContentsT], signatory: IdentifierKey): Signed[ContentsT] =
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
final case class SignedMatcher[ContentsT <: ValType]
(level: BigInt, document: Code[Val[ContentsT]], signature: Code[Val[Identifier]]) extends Val[Signed[ContentsT]]:
  assert(level > 0)
end SignedMatcher
// todo should only be allowed in code patterns (although maybe it's not such a big deal if it can be used elsewhere)
