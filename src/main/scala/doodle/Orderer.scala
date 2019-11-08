package doodle

object Orderer extends App {
  @specialized(Byte, Short, Int, Long, Float, Double)
  @inline def ordered[T: Ordering](a: T, b: T): (T, T) = {
    val ord = implicitly[Ordering[T]]; import ord._
    if (a <= b) (a, b) else (b, a)
  }

  @specialized(Byte, Short, Int, Long, Float, Double)
  @inline def ordered[T: Ordering](a: T, b: T, c: T): (T, T, T) = {
    val ord = implicitly[Ordering[T]]; import ord._
    @inline def helper(a: T, b: T): (T, T, T) = {
      if (b <= c) (a, b, c)
      else {
        val (first, second) = ordered(a, c)
        (first, second, b)
      }
    }
    if (a <= b) helper(a, b)
    else helper(b, a)
  }

  @specialized(Byte, Short, Int, Long, Float, Double)
  @inline def ordered[T: Ordering](a: T, b: T, c: T, d: T): (T, T, T, T) = {
    val ord = implicitly[Ordering[T]]; import ord._
    @inline def helper(a: T, b: T): (T, T, T, T) = {
      if (b <= c) ???
      else {
        ???
      }
    }
    if (a <= b) {

    }
    ???
  }

  trait PartialOrder[-T] {

  }
}
