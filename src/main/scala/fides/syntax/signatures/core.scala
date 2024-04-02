package fides.syntax.signatures

import fides.syntax.code.{Code, Expr, Ptrn, Val, ValQ, ValType, Xctr}
import fides.syntax.identifiers.{Identifier, IdentifierKey}
import fides.syntax.values.NaturalNumber

/**
  * Signed values are guaranteed to have been created using a key corresponding to [[signature]].
  *
  * @param document  the signed value
  * @param signature the identifier corresponding to the key that was used to sign the document
  * @tparam T the type of the signed value
  */
final case class Signed[+T <: ValType] private(document: Val[T], signature: Identifier) extends ValQ[Signed[T]], ValType
object Signed:
  /**
    * Signed values can only be created from keys, but only reveal the corresponding identifier.
    *
    * todo This is not meant to be used as part of the syntax...
    */
  def newInstance[T <: ValType](document: Val[T], signatory: IdentifierKey): Signed[T] =
    new Signed(document, signatory.identifier)
end Signed

/**
  * Since Signed values cannot be created freely, a different type of value is needed for matching.
  * To match the latter, yet a different one is needed, and so forth.
  * This is solved by having a level, effectively introducing a hierarchy of matchers.
  * This is similar to having to use a backslash in a regex to escape another backslash
  * (except that the number of backslashes needed grows exponentially with how meta the regex is).
  *
  * [[SignedMatcher]](0, m, s) matches [[Signed]](m, s).
  * For level > 0, [[SignedMatcher]](level, m, s) matches [[SignedMatcher]](level - 1, m, s).
  *
  * @tparam T keeps track of the value type
  */
final case class SignedMatcher[T <: ValType](
  document: Code[Val[T]],
  signature: Code[Val[Identifier]],
  level: Code[Val[NaturalNumber]] = NaturalNumber(0),
) extends ValQ[Signed[T]]
// todo should only be allowed in code patterns (although maybe it's not such a big deal if it can be used elsewhere)
// todo delete? Can it always be simulated with MatchEscape(MatchWrap(MatchSign(...)))?

/**
  * Primitive to sign values
  */
final case class Sign[T <: ValType](
  document: Code[Expr[T]],
  signatory: Code[Expr[IdentifierKey]],
) extends Expr[Signed[T]]

/**
  * Primitive to unsign values
  */
final case class UnSign[T <: ValType](
  document: Code[Xctr[T]],
  signature: Code[Xctr[Identifier]],
) extends Xctr[Signed[T]]

/**
  * Primitive to match signed values
  */
final case class MatchSign[P <: N, N <: ValType](
  document: Code[Ptrn[P, N]],
  signature: Code[Ptrn[Identifier, Identifier]],
) extends Ptrn[Signed[P], Signed[N]]
// todo should this really be allowed?
