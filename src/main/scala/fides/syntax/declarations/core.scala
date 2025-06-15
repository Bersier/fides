package fides.syntax.declarations
// todo rename package to "variables"

import fides.syntax.core.Code
import fides.syntax.types.{ChanT, DeclS, Expr, NameS, TopT, TypeS, Xctr}

// todo generalize Loc for both channels and variables?
final case class Use[T <: TopT](variableName: Code[NameS[T]]) extends Code[Expr[T]]

// todo merge Use and Assign into something like Loc
final case class Assign[T <: TopT](variableName: Code[NameS[T]]) extends Code[Xctr[T]]

object Declaration:
  final case class ImmutableVariable[T <: TopT](
    name: Code[NameS[T]],
    tipe: Code[TypeS[T]],
    body: Code[Expr[T]],
  ) extends Code[DeclS[T]]

  final case class MutableVariable[T <: TopT](
    name: Code[NameS[T]],
    tipe: Code[TypeS[T]],
    body: Code[Expr[T]],
  ) extends Code[DeclS[T]]

  // todo do we need a separate one for channels?
  //  We're supposed to execute anchor-free declaration bodies early anyways, no?
  //  We rather need to keep track of staticity in the type...
  final case class FreshChannel[T <: TopT](
    name: Code[NameS[ChanT[T, T]]],
  ) extends Code[DeclS[ChanT[T, T]]]
end Declaration
