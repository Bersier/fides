package doodle

import scala.collection.mutable

object Primes extends App {
  val slowPrimes: LazyList[Int] = LazyList.from(2).filter(n => (2 until n).forall(m => n % m != 0))
  val primes: LazyList[Int] = 2 #:: LazyList.from(3, 2).filter(n => primes.takeWhile(p => p <= math.sqrt(n)).forall(p => n % p > 0))

  val addends: Array[Int] = Array(6, 2, 6, 4, 2, 4, 2, 4)
  // val pre: LazyList[Int] = 23 #::

  def integrate(c: Int, seq: Seq[Int]): IndexedSeq[Int] = {
    val acc = new mutable.ArrayBuilder.ofInt
    @scala.annotation.tailrec
    def integrate(c: Int, seq: Seq[Int]): IndexedSeq[Int] = {
      acc.addOne(c)
      seq match {
        case Nil    => acc.result()
        case h +: t => integrate(c + h, t)
      }
    }
    integrate(c, seq)
  }

  for (p <- slowPrimes.slice(1000, 1010)) {
    println(p)
  }

  println("-----------")

  for (p <- primes.slice(100000, 100010)) {
    println(p)
  }
}
