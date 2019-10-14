package doodle

import scala.collection.mutable

trait RecFun[A, B] {
  type SelfT = A => B

  def rec(self: SelfT)(input: A): B

  final def apply(a: A): B = rec(apply)(a)

  def mem: A => B = {
    val map = mutable.Map.empty[A, B]

    def memF(a: A): B = {
      map.getOrElseUpdate(a, rec(memF)(a))
    }

    memF
  }

  def memWithCallCountPrint: A => B = {
    val map = mutable.Map.empty[A, B]
    var callCount = 0
    def memFWithCallCountPrint(a: A): B = {
      def memF(a: A): B = {
        map.getOrElseUpdate(a, {
          callCount += 1; rec(memF)(a)
        })
      }
      val result = memF(a)
      println("Call count: " + callCount)
      result
    }

    memFWithCallCountPrint
  }
}
