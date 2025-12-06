package fides.syntax.signatures

import fides.syntax.machinery.*

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
  document: OldCode[OldCnstG[D]],
  signature: OldCode[OldCnstG[IdentifierUD]],
) extends OldCode[OldCnstG[SignedD[D]]]

/**
  * Primitive to sign values
  */
final case class Sign[D <: TopD](
  document: OldCode[OldExprG[D]],
  signatory: OldCode[OldExprG[IdentifierKeyUD]],
) extends OldCode[ExvrG[SignedD[D]]]

/**
  * Primitive to match signed values
  */
final case class UnSign[D <: TopD](
  document: OldCode[OldXctrG[D]],
  signature: OldCode[OldXctrG[IdentifierUD]],
) extends OldCode[XcvrG[SignedD[D]]]
