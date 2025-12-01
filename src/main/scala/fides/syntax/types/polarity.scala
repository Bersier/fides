package fides.syntax.types

sealed trait Bool
type False = Bool
final abstract class True extends Bool

type TopP = Polarity[False, False, False]
final abstract class Polarity[+P <: Bool, +N <: Bool, +C <: Bool]
type BotP = Polarity[True, True, True]
