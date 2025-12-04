package fides.syntax.signatures

import fides.syntax.types.*

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
  document: OldCode[Cnst[T]],
  signature: OldCode[Cnst[IdentifierUT]],
) extends OldCode[Cnst[SignedT[T]]]

/**
  * Primitive to sign values
  */
final case class Sign[T <: TopT](
  document: OldCode[Expr[T]],
  signatory: OldCode[Expr[IdentifierKeyUT]],
) extends OldCode[Exvr[SignedT[T]]]

/**
  * Primitive to match signed values
  */
final case class UnSign[T <: TopT](
  document: OldCode[Xctr[T]],
  signature: OldCode[Xctr[IdentifierUT]],
) extends OldCode[Xcvr[SignedT[T]]]
