package util

import scala.compiletime.ops.boolean.{&&, ||}
import scala.compiletime.ops.int.+

/**
  * Assumes that neither L1 nor L2 have repeated elements.
  */
type AreSameSet[L1 <: TList[?], L2 <: TList[?]] <: Boolean = AreSameSize[L1, L2] match
  case false => false
  case true => IsSubset[L1, L2]

type IsSubset[L1 <: TList[?], L2 <: TList[?]] <: Boolean = L1 match
  case TList.Empty => true
  case TList.Cons[?, h1, tail1] => Contains[L2, h1] && IsSubset[tail1, L2]

type HasRepeats[L <: TList[?]] <: Boolean = L match
  case TList.Empty => false
  case TList.Cons[?, h, tail] => Contains[tail, h] || HasRepeats[tail]

type Contains[L <: TList[?], U] <: Boolean = L match
  case TList.Empty => false
  case TList.Cons[?, U, ?] => true
  case TList.Cons[?, ?, tail] => Contains[tail, U]

type Size[L <: TList[?]] <: Int = L match
  case TList.Empty => 0
  case TList.Cons[?, ?, tail] => Size[tail] + 1

type AreSameSize[L1 <: TList[?], L2 <: TList[?]] <: Boolean = L1 match
  case TList.Empty => L2 match
    case TList.Empty => true
    case _           => false
  case TList.Cons[?, ?, tail1] => L2 match
    case TList.Empty => false
    case TList.Cons[?, ?, tail2] => AreSameSize[tail1, tail2]
