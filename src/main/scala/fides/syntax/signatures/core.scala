package fides.syntax.signatures

import fides.syntax.core.Code
import fides.syntax.types.{Cnst, Expr, Exvr, IdentifierKeyT, IdentifierT, SignedT, TopT, Xctr, Xcvr}

/**
  * Signed values are guaranteed to have been created using a key corresponding to [[signature]].
  *
  * Signed values can only be created from keys, but only reveal the corresponding identifier.
  *
  * @param document  the signed value
  * @param signature the identifier corresponding to the key that was used to sign the document
  * @tparam T the type of the signed value
  */
final case class Signed[T <: TopT] private[fides] (
  document: Code[Cnst[T]],
  signature: Code[Cnst[IdentifierT]],
) extends Code[Cnst[SignedT[T]]]

/**
  * Primitive to sign values
  */
final case class Sign[T <: TopT](
  document: Code[Expr[T]],
  signatory: Code[Expr[IdentifierKeyT]],
) extends Code[Exvr[SignedT[T]]]

/**
  * Primitive to match signed values
  */
final case class UnSign[T <: TopT](
  document: Code[Xctr[T]],
  signature: Code[Xctr[IdentifierT]],
) extends Code[Xcvr[SignedT[T]]]
