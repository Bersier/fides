package doodle

import scala.collection.mutable

trait RecFun[A, B] {
  type selfT = A => B

  def rec(self: selfT)(a: A): B

  final def apply(a: A): B = rec(apply)(a)

  def mem: A => B = {
    val map = mutable.Map.empty[A, B]

    def memF(a: A): B = {
      map.getOrElseUpdate(a, rec(memF)(a))
    }

    memF
  }
}
