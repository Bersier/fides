package fides.syntax.types

type TopQ = GenericQ[TopS]
sealed trait GenericQ[+S <: TopS]

final abstract class NormalQ[+S <: TopS] extends GenericQ[S]

final abstract class EscapeQ[+S <: TopS, +Q <: TopQ, Level] extends GenericQ[S]

// todo variance
// todo add M here instead?
