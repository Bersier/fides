package fides.syntax.machinery

sealed trait TopF private[machinery]()

sealed trait PolarF[+V <: TopV] extends TopF

sealed trait PairF[+F1 <: TopF, +F2 <: TopF, +V <: TopV] extends PolarF[V]

final abstract class PolarBotF[+V <: TopV] extends PairF[Nothing, Nothing, V]
