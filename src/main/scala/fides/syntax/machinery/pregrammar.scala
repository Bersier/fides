package fides.syntax.machinery

sealed trait TopF private[machinery]()

sealed trait PolarF[+V <: TopV] extends TopF

sealed trait PairF[
  +V <: TopV,
  +F1 <: TopF, +F2 <: TopF,
] extends PolarF[V]

final abstract class PolarBotF[+V <: TopV] extends PairF[V, Nothing, Nothing]
