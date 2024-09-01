package fides.syntax.signatures

import fides.syntax.code.Polarity.Negative
import fides.syntax.code.{Code, Expr, Polar, Ptrn, Val, ValType, Xctr}
import fides.syntax.identifiers.{Identifier, IdentifierKey}

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
  *
  * [[UnSign]]`[T] <: `[[Xctr]]`[`[[Signed]]`[T]]`
  */
type UnSign[T <: ValType] = MatchSign[Nothing, T, Nothing, Signed[T]]
object UnSign:
  inline def apply[T <: ValType](
    inline document: Code[Xctr[T]],
    inline signature: Code[Xctr[Identifier]],
  ): UnSign[T] = MatchSign(document, signature)
end UnSign

/**
  * Primitive to match signed values
  */
final case class MatchSign[
  P <: N,
  N <: ValType,
  L >: Nothing <: Signed[P],
  U >: Signed[N] <: ValType,
](
  document: Code[Ptrn[P, N]],
  signature: Code[Ptrn[Identifier, Identifier]],
)(using
  Signed[P] <:< (L | Signed[Nothing]),
  (U & Signed[ValType]) <:< Signed[N],
) extends Polar[Negative, L, U]
