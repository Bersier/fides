package fides.syntax.types

type TopC = Code3[TopS, TopM]

trait Code3[+S <: TopS, +M <: TopM] private[syntax]()
