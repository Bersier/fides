package doodle

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

  timePrimes(10000, 10, primesV0)
  timePrimes(1000000, 10, primesV1)
  timePrimes(1000000, 10, primesV2)
  timePrimes(1000000, 10, primesV3)

  def timePrimes(dropCount: Int, takeCount: Int, primes: LazyList[Int]): Unit = {
    val start = System.currentTimeMillis
    val result: List[Int] = primes.drop(dropCount).take(takeCount).toList
    val stop = System.currentTimeMillis
    println("(time: " + (stop - start) + ") " + result)
  }
}
