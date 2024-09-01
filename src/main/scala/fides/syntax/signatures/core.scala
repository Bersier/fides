package fides.syntax.signatures

import fides.syntax.code.{Code, Expr, Ptrn, Val, ValType, Xctr}
import fides.syntax.identifiers.{Identifier, IdentifierKey}
import fides.syntax.values.NaturalNumber

/**
  * Signed values are guaranteed to have been created using a key corresponding to [[signature]].
  *
  * @param document  the signed value
  * @param signature the identifier corresponding to the key that was used to sign the document
  * @tparam T the type of the signed value
  */
final case class Signed[+T <: ValType] private(document: Val[T], signature: Identifier) extends Val[Signed[T]], ValType
object Signed:
  /**
    * Signed values can only be created from keys, but only reveal the corresponding identifier.
    */
  private[fides] def newInstance[T <: ValType](document: Val[T], signatory: IdentifierKey): Signed[T] =
    new Signed(document, signatory.identifier)
end Signed

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
// todo fix type, similarly to MatchPair
