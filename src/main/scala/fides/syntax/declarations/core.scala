package fides.syntax.declarations
// todo rename package to "variables"

import fides.syntax.types.*

// todo generalize Loc for both channels and variables?
final case class Use[T <: TopT](variableName: OldCode[NameS[T]]) extends OldCode[Exvr[T]]

// todo merge Use and Assign into something like Loc
final case class Assign[T <: TopT](variableName: OldCode[NameS[T]]) extends OldCode[Xcvr[T]]

object Declaration:
  final case class ImmutableVariable[T <: TopT](
    name: OldCode[NameS[T]],
    tipe: OldCode[TypeS[T]],
    body: OldCode[Expr[T]],
  ) extends OldCode[DeclS[T]]

  final case class MutableVariable[T <: TopT](
    name: OldCode[NameS[T]],
    tipe: OldCode[TypeS[T]],
    body: OldCode[Expr[T]],
  ) extends OldCode[DeclS[T]]

  // todo do we need a separate one for channels?
  //  We're supposed to execute anchor-free declaration bodies early anyways, no?
  //  We rather need to keep track of staticity in the type...
  final case class FreshChannel[T <: TopT](
    name: OldCode[NameS[ChanT[T, T]]],
  ) extends OldCode[DeclS[ChanT[T, T]]]
end Declaration
