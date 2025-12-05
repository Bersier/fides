package fides.syntax.declarations
// todo rename package to "variables"

import fides.syntax.types.*

// todo generalize Loc for both channels and variables?
final case class Use[D <: TopD](variableName: OldCode[NameS[D]]) extends OldCode[Exvr[D]]

// todo merge Use and Assign into something like Loc
final case class Assign[D <: TopD](variableName: OldCode[NameS[D]]) extends OldCode[Xcvr[D]]

object Declaration:
  final case class ImmutableVariable[D <: TopD](
    name: OldCode[NameS[D]],
    tipe: OldCode[TypeS[D]],
    body: OldCode[Expr[D]],
  ) extends OldCode[DeclS[D]]

  final case class MutableVariable[D <: TopD](
    name: OldCode[NameS[D]],
    tipe: OldCode[TypeS[D]],
    body: OldCode[Expr[D]],
  ) extends OldCode[DeclS[D]]

  // todo do we need a separate one for channels?
  //  We're supposed to execute anchor-free declaration bodies early anyways, no?
  //  We rather need to keep track of staticity in the type...
  final case class FreshChannel[D <: TopD](
    name: OldCode[NameS[ChanD[D, D]]],
  ) extends OldCode[DeclS[ChanD[D, D]]]
end Declaration
