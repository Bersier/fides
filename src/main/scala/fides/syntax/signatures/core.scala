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
  document: OldCode[Cnst[D]],
  signature: OldCode[Cnst[IdentifierUD]],
) extends OldCode[Cnst[SignedD[D]]]

/**
  * Primitive to sign values
  */
final case class Sign[D <: TopD](
  document: OldCode[Expr[D]],
  signatory: OldCode[Expr[IdentifierKeyUD]],
) extends OldCode[Exvr[SignedD[D]]]

/**
  * Primitive to match signed values
  */
final case class UnSign[D <: TopD](
  document: OldCode[Xctr[D]],
  signature: OldCode[Xctr[IdentifierUD]],
) extends OldCode[Xcvr[SignedD[D]]]
