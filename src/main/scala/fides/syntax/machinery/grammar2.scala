package fides.syntax.machinery

sealed trait Gen2G[+W <: TopW]

final case class Pair2G[
  +W <: TopW,
  +W1 <: TopW, +W2 <: TopW,
](g1: Gen2G[W1], g2: Gen2G[W2])(using PairW[W1, W2, W]) extends Gen2G[W]
