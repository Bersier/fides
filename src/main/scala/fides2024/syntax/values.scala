package fides2024.syntax

// todo make Val[T] equivalent to Quotation[Val[T]]?

/**
  * A value that doesn't carry any information beyond causality (the sending of any value occurs before its reception).
  *
  * The corresponding type, U.type, is like the Unit type in Fides.
  */
object U extends Val[U.type]

/**
  * Boolean values
  */
sealed trait Bool extends Val[Bool]
object True extends Val[Bool]
object False extends Val[Bool]

/**
  * A value that is made up of two values.
  */
final case class Paired[T1 <: ValType, T2 <: ValType]
(first: Code[Val[T1]], second: Code[Val[T2]]) extends Val[Paired[T1, T2]]

/**
  * A value that is made up of an unordered group of values.
  */
sealed trait Collection[T <: ValType] extends Val[Collection[T]]:
  def elements: Iterable[Val[T]]
end Collection
object Empty extends Collection[Nothing]:
  def elements: Iterable[Val[Nothing]] = Iterable.empty[Val[Nothing]]
end Empty
final case class NonEmpty[T <: ValType](elements: Val[T]*) extends Collection[T]:
  assert(elements.nonEmpty)
end NonEmpty

/**
  * Code as value, used for metaprogramming
  */
final case class Quotation[+C <: CodeType](code: Code[C]) extends Val[Quotation[C]]

/**
  * Identifiers are structureless. They can only be compared for equality. They cannot be inspected in any other way.
  * New identifiers can be created. It is not possible to construct identifiers in any other way.
  */
final class Location[T <: ValType] extends Val[Location[T]] derives CanEqual
// todo add Symbol?
// todo add way to get reflected T, maybe using izumi-reflect
// todo use context functions?
// todo should Location also have a LocType?

type Identifier = Location[ValType]
object Identifier:
  def apply(): Identifier = Location()
end Identifier

/**
  * A key has a corresponding identifier. The identifer can be obtained from the key, but not vice versa
  * (there might not even exist a corresponding key for some identifiers).
  */
final class IdentifierKey extends Val[IdentifierKey]:
  val identifier: Identifier = new Identifier
end IdentifierKey

/**
  * Signed values are guaranteed to have been created using a key corresponding to @signature.
  *
  * @param document the signed value
  * @param signature the identifier corresponding to the key that was used to sign the document
  * @tparam T the type of the signed value
  */
final case class Signed[+T <: ValType] private(document: Val[T], signature: Identifier) extends Val[Signed[T]]
object Signed:
  /**
    * Signed values can only be created from keys, but only reveal the corresponding identifier.
    */
  def apply[T <: ValType](document: Val[T], signatory: IdentifierKey): Signed[T] =
    new Signed(document, signatory.identifier)
end Signed

/**
  * Since Signed values cannot be created freely, a different type of value is needed for matching.
  * To match the latter, yet a different one is needed, and so forth.
  * This is solved by having a level, effectively introducing a hierarchy of matchers.
  * This is similar to having to use a backslash in a regex to escape another backslash
  * (except that the number of backslashes needed grows exponentially with how meta the regex is).
  *
  * SignedMatcher(1, m, s) matches Signed(m, s).
  * For level > 1, SignedMatcher(level, m, s) matches SignedMatcher(level - 1, m, s).
  */
final case class SignedMatcher[T <: ValType]
(level: BigInt, document: Code[Val[T]], signature: Code[Val[Identifier]]) extends Val[Signed[T]]:
  assert(level > 0)
end SignedMatcher
// todo should only be allowed in code patterns (although maybe it's not such a big deal if it can be used elsewhere)
