package fides.syntax.signatures

import fides.syntax.types.*

/**
  * Signed values are guaranteed to have been created using a key corresponding to [[signature]].
  *
  * Signed values can only be created from keys, but only reveal the corresponding identifier.
  *
  * @param document  the signed value
  * @param signature the identifier corresponding to the key that was used to sign the document
  * @tparam D the type of the signed value
  */
final case class Signed[D <: TopD] private[fides] (
  document: OldCode[CnstS[D]],
  signature: OldCode[CnstS[IdentifierUD]],
) extends OldCode[CnstS[SignedD[D]]]

/**
  * Primitive to sign values
  */
final case class Sign[D <: TopD](
  document: OldCode[ExprS[D]],
  signatory: OldCode[ExprS[IdentifierKeyUD]],
) extends OldCode[ExvrS[SignedD[D]]]

/**
  * Primitive to match signed values
  */
final case class UnSign[D <: TopD](
  document: OldCode[XctrS[D]],
  signature: OldCode[XctrS[IdentifierUD]],
) extends OldCode[XcvrS[SignedD[D]]]
