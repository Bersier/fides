package doodle

import scala.collection.LinearSeq

//noinspection DropTakeToSlice
object Primes extends App {
  val primesV0: LazyList[Int] = LazyList.from(2).filter(n => (2 until n).forall(m => n % m != 0))
  val primesV1: LazyList[Int] = 2 #:: LazyList.from(3, 2)
    .filter(n => primesV1.takeWhile(p => p <= math.sqrt(n)).drop(1).forall(p => n % p > 0))

  val repeatedAddends: LazyList[Int] = LazyList(2, 4, 2, 4, 6, 2, 6, 4) #::: repeatedAddends

  //noinspection ForwardReference
  val primesV2: LazyList[Int] =
    LazyList(2, 3, 5, 7) #::: series(11, repeatedAddends)
      .filter(n => primesV2Tail.takeWhile(p => p <= math.sqrt(n)).forall(p => n % p > 0))
  val primesV2Tail: LazyList[Int] = primesV2.drop(3)

  val primesV3: LazyList[Int] = LazyList(2, 3, 5, 7) #::: primeFilter(series(11, repeatedAddends), primesV3.drop(3))
  def primeFilter(remainingCandidates: LazyList[Int], remainingDividers: LazyList[Int]): LazyList[Int] = {
    val nextCandidate +: otherCandidates = remainingCandidates
    val nextDivider +: otherDividers = remainingDividers
    val nextDSquared = nextDivider * nextDivider

    require(nextCandidate <= nextDSquared)
    if (nextDSquared == nextCandidate) primeFilter(otherCandidates.filter(n => n % nextDivider > 0), otherDividers)
    else nextCandidate #:: primeFilter(otherCandidates, remainingDividers)
  }

  def series(s0: Int, infiniteSeq: LazyList[Int]): LazyList[Int] = {
    val s1 +: t = infiniteSeq
    s0 #:: series(s0 + s1, t)
  }

//  timePrimes(10000, 10, primesV0)
//  timePrimes(1000000, 10, primesV1)
//  timePrimes(1000000, 10, primesV2)
//  timePrimes(1000000, 10, primesV3)

  println(primesV1.take(34).toList)
  println(difference(primesV1).take(33).toList)

  def timePrimes(dropCount: Int, takeCount: Int, primes: LazyList[Int]): Unit = {
    val start = System.currentTimeMillis
    val result: List[Int] = primes.drop(dropCount).take(takeCount).toList
    val stop = System.currentTimeMillis
    println("(time: " + (stop - start) + ") " + result)
  }

  def difference(seq: Seq[Int]): LazyList[Int] = seq match {
    case head +: tail => difference(head, tail)
    case _            => LazyList()
  }

  def difference(head: Int, seq: Seq[Int]): LazyList[Int] = seq match {
    case newHead +: tail => (newHead - head) #:: difference(newHead, tail)
    case _               => LazyList()
  }

  var m = BigInt(1)
  var n = 2
  var l = LazyList(1)
  for (i <- 1 to 40) {
    l = unfold(n, l, m)
//    println("n = " + n + ", length = " + l.size)
    println("n = " + n + ", Unfold: " + l.take(40).toList)
    m *= n
    n = primesV1(i)
  }

  def unfold(n: Int, wheel: LinearSeq[Int], m: BigInt): LazyList[Int] = {
    require(wheel.nonEmpty)
    def loop(downCounter: BigInt, remaining: Int, wheel: LazyList[Int], intermediate: Int): LazyList[Int] = {
      require(downCounter >= 0)
      require(remaining >= 0)
      if (downCounter == 0) LazyList()
      else {
        val jump +: tail = wheel
        val remainder = (jump + n - remaining) % n
        val newRemaining = n - remainder
        val newDownCounter = downCounter - jump
        if (remainder == 0) loop(newDownCounter, newRemaining, tail, jump)
        else (intermediate + jump) #:: loop(newDownCounter, newRemaining, tail, 0)
      }
    }
    loop(n * m, n - 1, repeated(wheel), 0)
  }

  // TODO lazy val primes and primeGaps defined mutualcorecursively
  def primeGaps(n: Int, wheel: LinearSeq[Int], m: BigInt): LazyList[Int] = {
    require(wheel.nonEmpty)
    def loop(remaining: Int, wheel: LazyList[Int], intermediate: Int): LazyList[Int] = {
      require(remaining >= 0)
      val jump +: tail = wheel
      val remainder = (jump + n - remaining) % n
      val newRemaining = n - remainder
      if (remainder == 0) loop(newRemaining, tail, jump)
      else (intermediate + jump) #:: loop(newRemaining, tail, 0)
    }
    loop(n - 1, repeated(wheel), 0)
  }

  def repeat(count: Int, array: Array[Int]): Array[Int] = {
    val length = array.length
    val result = Array.ofDim[Int](count * length)
    for (i <- 0 until count) {
      Array.copy(array, 0, result, i * length, length)
    }
    result
  }

  def repeated[A](seq: LinearSeq[A]): LazyList[A] = {
    require(seq.nonEmpty)
    def loop(rem: LinearSeq[A]): LazyList[A] = rem match {
      case LinearSeq()  => loop(seq)
      case head +: tail => head #:: loop(tail)
    }
    loop(seq)
  }

  // TODO previous repeated leaks memory if head is kept
//  def repeated[A](seq: LinearSeq[A]): LinearSeq[A] = {
//    require(seq.nonEmpty)
//    new LinearSeq[A] {
//      override def apply(i: Int): A = ???
//
//      override def length: Int = ???
//
//      override def iterator: Iterator[A] = ???
//    }
//  }
}
