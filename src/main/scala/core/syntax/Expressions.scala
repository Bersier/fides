package core.syntax

final case class Merge[+K <: N, T <: A](one: L[K, Inp[Bag[K, _ <: T, Inp]]], two: L[K, Inp[Bag[K, _ <: T, Inp]]])
  extends L[K, Inp[Bag[K, T, Inp]]]

final case class Contains[+K <: N, T <: A](bag: L[K, Inp[Bag[K, _ <: T, Inp]]], element: L[K, Inp[T]])
  extends L[K, Inp[Bag[K, T, Inp]]]

final case class Equals[+K <: N, +T <: A](one: L[K, Inp[T]], two: L[K, Inp[T]]) extends L[K, Inp[BoolVal]]

final case class Branch[+K <: N, -T <: A](one: L[K, Out[T]], two: L[K, Out[T]]) extends L[K, Out[BoolVal]]
// Do we really need stuff like that?

final case class ForgetInp[+K <: N](expr: L[K, Inp[_]]) extends L[K, Inp[U]]
final case class ForgetOut[+K <: N](expr: L[K, Out[U]]) extends L[K, Out[Nothing]]

final case class Copy[K <: N, T <: A](outs: L[K, Val[Bag[K, _ >: T, Out]]]) extends L[K, Out[T]]

final case class Apply[+K <: N, S >: U, T](input: L[K, Inp[ScalaVal[_ <: S]]], f: S => T)
  extends L[K, Inp[ScalaVal[_ >: T]]]
final case class Convert[+K <: N, T, +C[+_ <: A] <: X](v: L[K, C[SimT[T]]]) extends L[K, C[ScalaVal[T]]]
