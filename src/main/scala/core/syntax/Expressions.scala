package core.syntax

final case class Merge[+K <: N, +T <: A](one: L[K, Inp[Bag[K, T, Inp]]], two: L[K, Inp[Bag[K, T, Inp]]])
  extends L[K, Inp[Bag[K, T, Inp]]]

final case class Contains[+K <: N, +T <: A](bag: L[K, Inp[Bag[K, T, Inp]]], element: L[K, Inp[T]])
  extends L[K, Inp[Bag[K, T, Inp]]]

final case class Equals[+K <: N, +T <: A](one: L[K, Inp[T]], two: L[K, Inp[T]]) extends L[K, Inp[BoolVal]]

final case class Branch[+K <: N, +T <: A](one: L[K, Out[T]], two: L[K, Out[T]]) extends L[K, Out[BoolVal]]
// Do we really need stuff like that?

final case class ForgetInp[+K <: N](expr: L[K, Inp[_]]) extends L[K, Inp[U]]
final case class ForgetOut[+K <: N](expr: L[K, Out[U]]) extends L[K, Out[Nothing]]

final case class Copy[+K <: N, +T <: A](outs: L[K, Val[Bag[K, T, Out]]]) extends L[K, Out[T]]

final case class Apply1[+K <: N](i: L[K, Inp[Z]], f: BigInt => BigInt) extends L[K, Inp[Z]]
final case class Apply2[+K <: N](i1: L[K, Inp[Z]], i2: L[K, Inp[Z]], f: (BigInt, BigInt) => BigInt)
  extends L[K, Inp[Z]]
final case class Apply3[+K <: N](
  i1: L[K, Inp[Z]],
  i2: L[K, Inp[Z]],
  i3: L[K, Inp[Z]],
  f : (BigInt, BigInt, BigInt) => BigInt,
) extends L[K, Inp[Z]]

final case class ApplyP1[+K <: N](i: L[K, Inp[Z]], f: BigInt => Boolean) extends L[K, Inp[BoolVal]]
final case class ApplyP2[+K <: N](
  i1: L[K, Inp[Z]],
  i2: L[K, Inp[Z]],
  f: (BigInt, BigInt) => Boolean
) extends L[K, Inp[BoolVal]]
// Add more (eg boolean functions)?
