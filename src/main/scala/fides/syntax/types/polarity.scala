package fides.syntax.types

type Bool = False
sealed trait False
final abstract class True extends False

type TopP = Polarity[False, False, False]
final abstract class Polarity[+P <: Bool, +N <: Bool, +C <: Bool]
