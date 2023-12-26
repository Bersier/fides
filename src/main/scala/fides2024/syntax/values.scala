package fides2024.syntax

object Unit extends Val[Unit.type]
object True extends Val[True.type]
object False extends Val[False.type]

final case class Pair[FirstT <: ValSup, SecondT <: ValSup]
(first: Val[FirstT], second: Val[SecondT]) extends Val[Pair[FirstT, SecondT]]

// sealed trait Collection[ElementT <: F[K]] extends F[Collection[K]]
sealed trait Collection[ElementT <: ValSup] extends Val[Collection[ElementT]]
object Empty extends Collection[Nothing]
final case class NonEmpty[ElementT <: ValSup](elements: Iterable[ElementT]) extends Collection[ElementT]:
  assert(elements.nonEmpty)
end NonEmpty

/**
  * Code as value, used for metaprogramming.
  */
final case class Quotation(code: Component) extends Val[Quotation]

final case class Escape[P <: Polarity](code: Expr[P, ValSup]) extends Val[Nothing]

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
final case class Signed[ContentsT <: ValSup] private
(document: Val[ContentsT], signature: Identifier) extends Val[Signed[ContentsT]]
object Signed:
  /**
    * Signed values can only be created from keys, but only reveal the corresponding identifier.
    */
  def apply[ContentsT <: ValSup](document: Val[ContentsT], signatory: IdentifierKey): Signed[ContentsT] =
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
final case class SignedMatcher[ContentsT <: ValSup]
(level: BigInt, document: Val[ContentsT], signature: Val[Identifier]) extends Val[Signed[ContentsT]]:
  assert(level > 0)
end SignedMatcher
// todo should only be allowed in code patterns (although maybe it's not such a big deal if it can be used elsewhere)
