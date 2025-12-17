package fides.syntax.machinery

type Top2G = Gen2G[TopW]
sealed trait Gen2G[+W <: TopW]

final case class Pair2G[
  +W1 <: TopW, +W2 <: TopW, +W <: TopW,
](g1: Gen2G[W1], g2: Gen2G[W2])(using PairW[W1, W2, W]) extends Gen2G[W]
