package fides.syntax.declarations
// todo rename package to "variables"

import fides.syntax.machinery.*

// todo generalize Loc for both channels and variables?
final case class Use[D <: TopD](variableName: OldCode[NameG[D]]) extends OldCode[ExvrG[D]]

// todo merge Use and Assign into something like Loc
final case class Assign[D <: TopD](variableName: OldCode[NameG[D]]) extends OldCode[XcvrG[D]]

object Declaration:
  final case class ImmutableVariable[D <: TopD](
    name: OldCode[NameG[D]],
    tipe: OldCode[TypeG[D]],
    body: OldCode[ExprG[D]],
  ) extends OldCode[DeclG[D]]

  final case class MutableVariable[D <: TopD](
    name: OldCode[NameG[D]],
    tipe: OldCode[TypeG[D]],
    body: OldCode[ExprG[D]],
  ) extends OldCode[DeclG[D]]

  // todo do we need a separate one for channels?
  //  We're supposed to execute anchor-free declaration bodies early anyways, no?
  //  We rather need to keep track of staticity in the type...
  final case class FreshChannel[D <: TopD](
    name: OldCode[NameG[ChanD[D, D]]],
  ) extends OldCode[DeclG[ChanD[D, D]]]
end Declaration
