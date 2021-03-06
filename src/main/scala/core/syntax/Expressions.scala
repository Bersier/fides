package core.syntax

final case class Merge[+K <: N, +S <: Sort, T <: A](
  one: L[K, _, Inp[Bag[_, S, Inp, T]]],
  two: L[K, _, Inp[Bag[_, S, Inp, T]]],
) extends L[K, S#R, Inp[Bag[_, S, Inp, T]]]

final case class Contains[+K <: N, +S <: Sort, T <: A](bag: L[K, S, Inp[Bag[_, _, Inp, T]]], element: L[K, S, Inp[T]])
  extends L[K, S#R, Inp[BoolVal]]

final case class Equals[+K <: N, +T <: A](one: L[K, Inp[T]], two: L[K, Inp[T]]) extends L[K, Inp[BoolVal]]
// Hash...

// branch (a ->
//        (b ->


final case class Branch[+K <: N, -T <: A](one: L[K, Out[T]], two: L[K, Out[T]]) extends L[K, Out[BoolVal]]
// Do we really need stuff like that?

final case class ForgetInp[+K <: N](expr: L[K, Inp[_]]) extends L[K, Inp[U]]
final case class ForgetOut[+K <: N](expr: L[K, Out[U]]) extends L[K, Out[Nothing]]

final case class Copy[K <: N, T <: A](outs: L[K, Out[Bag[K, _ >: T, Out]]]) extends L[K, Out[T]]

final case class Apply[+K <: N, S, T](input: L[K, Inp[ScalaVal[_ <: S]]], f: S => T) extends L[K, Inp[ScalaVal[T]]]
final case class Convert[+K <: N, T, +C[+_ <: A] <: X](v: L[K, C[SimT[T]]]) extends L[K, C[ScalaVal[T]]]

///final case class Map(out: Out T) extends L[...Bag T]
// Need New at the expression level? (To avoid bottleneck?)
