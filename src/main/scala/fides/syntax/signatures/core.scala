package fides.syntax.signatures

import fides.syntax.core.Code
import fides.syntax.types.{Expr, IdentifierKeyT, IdentifierT, SignedT, ValTop, Xctr}

///**
//  * Signed values are guaranteed to have been created using a key corresponding to [[signature]].
//  *
//  * @param document  the signed value
//  * @param signature the identifier corresponding to the key that was used to sign the document
//  * @tparam T the type of the signed value
//  */
//final case class Signed[+T <: ValTop] private(document: Lit[T], signature: Identifier) extends Lit[Signed[T]], ValTop
//object Signed:
//  /**
//    * Signed values can only be created from keys, but only reveal the corresponding identifier.
//    */
//  private[fides] def newInstance[T <: ValTop](document: Lit[T], signatory: IdentifierKey): Signed[T] =
//    new Signed(document, signatory.identifier)
//end Signed

/**
  * Primitive to sign values
  */
final case class Sign[T <: ValTop](
  document: Code[Expr[T]],
  signatory: Code[Expr[IdentifierKeyT]],
) extends Code[Expr[SignedT[T]]]

/**
  * Primitive to match signed values
  */
final case class UnSign[T <: ValTop](
  document: Code[Xctr[T]],
  signature: Code[Xctr[IdentifierT]],
) extends Code[Xctr[SignedT[T]]]
