package doodle

import scala.collection.immutable.LinearSeq
import scala.util.control.TailCalls._

object LargestPossibleProfit extends App {

  private val base1 = 3
  private val base2 = 5
  //  println(solution(Iterator.fill(1000000000)(Random.nextDouble())))
//  println(continuedFraction(math.log(10)/math.log(2)).take(10).toList)
  private val closestPowersOf2And3: LazyList[(Int, Int)] = closestPowers(base1, base2)
  private val closestPowersOf2And3X: LazyList[(Int, Int)] = closestPowersX(base1, base2)
  for (p <- closestPowersOf2And3X.take(14)) {
    print(p)
  }
  println()
  for (p <- closestPowersOf2And3.take(14)) {
    print(p)
  }

  def solution(prices: IterableOnce[Double]): Double = {
    var currentLow = Double.PositiveInfinity
    var currentHigh = Double.NegativeInfinity
    var currentBest = Double.NegativeInfinity
    for (p <- prices) {
      currentHigh = math.max(currentHigh, p)
      if (p < currentLow) {
        currentBest = math.max(currentBest, currentHigh - currentLow)
        currentLow = p
        currentHigh = Double.NegativeInfinity
      }
    }
    math.max(currentBest, currentHigh - currentLow)
  }

  def randomIntSeq(randomInt: => Int, generatorBound: Int)(count: Int, until: Int): Array[Int] = {
    // get power
    ???
  }

  def continuedFraction(d: Double): LazyList[Long] = {
    val first = math.floor(d).toLong
    val denominator = d - first
    if (denominator == 0) LazyList(first)
    else first #:: continuedFraction(1 / denominator)
  }

  def toFraction(continuedFraction: LinearSeq[Long]): Two[BigInt] = continuedFraction match {
    case Seq() => Two(1, 0)
    case h +: t => {
      val Two(b, a) = toFraction(t)
      val numerator = b * h + a
      Two(numerator, b).map(_ / numerator.gcd(b))
    }
  }

  def closestPowers(base1: Int, base2: Int): LazyList[(Int, Int)] = {
    def helper(m1: BigInt, m2: BigInt, p1: Int, p2: Int, bestPerformanceSoFar: Double): TailRec[LazyList[(Int, Int)]] = {
      if (m1 == m2) done(LazyList((p1, p2)))
      else {
        val performance = fraction((m1 - m2).abs, m1.min(m2))
        def tail(newBestPerformance: Double): TailRec[LazyList[(Int, Int)]] = {
          if (m1 < m2) tailcall(helper(m1 * base1, m2, p1 + 1, p2, newBestPerformance))
          else tailcall(helper(m1, m2 * base2, p1, p2 + 1, newBestPerformance))
        }
        if (performance < bestPerformanceSoFar) {
          done((p1, p2) #:: tail(performance).result)
        }
        else tailcall(tail(bestPerformanceSoFar))
      }
    }
    helper(base1, base2, 1, 1, Double.PositiveInfinity).result
  }

  def closestPowersX(base1: Int, base2: Int): LazyList[(Int, Int)] = {
    // TODO only correct at the beginning
    val continuedF: LazyList[Long] = continuedFraction(math.log(base2) / math.log(base1))
    def helper(continuedFLength: Int, bestPerformanceSoFar: Double): TailRec[LazyList[(Int, Int)]] = {
      // TODO avoid recalculating stuff
      val Two(numerator, denominator) = toFraction(continuedF.take(continuedFLength))
      val (p1, p2) = (numerator.toInt, denominator.toInt)
      // TODO no need to compute the full powers to find out how close they are (take log? or divide? ...)
      val m1 = BigInt(base1).pow(p1)
      val m2 = BigInt(base2).pow(p2)
      if (m1 == m2) done(LazyList((p1, p2)))
      else {
        val performance = fraction((m1 - m2).abs, m1.min(m2))
        def tail(newBestPerformance: Double): TailRec[LazyList[(Int, Int)]] = {
          tailcall(helper(continuedFLength + 1, newBestPerformance))
        }
        if (performance < bestPerformanceSoFar) {
          done((p1, p2) #:: tail(performance).result)
        }
        else tailcall(tail(bestPerformanceSoFar))
      }
    }
    helper(1, Double.PositiveInfinity).result
  }

  def fraction(a: BigInt, b: BigInt): Double = {
    val (quotient, remainder) = a /% b
    val (quotient1, _) = (remainder << 62) /% b
    quotient.toDouble + quotient1.toDouble / (1L << 62)
  }

  def sum(a: Int, b: Int): (Int, Int) = {
    val s = a + b
    if (a > 0) {
      if (b > 0) {
        if (s < 0) (s + Int.MinValue, 1)
        else (s, 0)
      }
      else (s, 0)
    }
    else {
      if (b < 0) {
        if (s >= 0) (s + Int.MinValue, -1)
        else (s, 0)
      }
      else (s, 0)
    }
  }

//  def randomInt(randomInt: => Int, generatorBound: Int)(until: Int): Int = {
//    def randomCoprimeInt(start: Int, startBound: Int, until: Int): Int = {
//
//      ???
//    }
//
//    val neededPower = ???
//    val powerBound = ???
//
//    val firstRandom = ???
//
//    val greatestCommonDivisor = gcd(powerBound, until)
//
//    if (firstRandom < powerBound - (powerBound % until)) firstRandom
//    else {
//      val untilQuotient = ???
//      val sharedRandom = ???
//      val randomQuotient = ???
//      val start = randomQuotient % untilQuotient
//      // TODO...
//      sharedRandom + greatestCommonDivisor * randomCoprimeInt(start, ???, ???)
//    }
//  }

  def gcd(a: Int, b: Int): Int = {
    def helper(a: Int, b: Int): Int = {
      val c = a - b
      val d = c >> lowestSetBit(c)
      if (d < b) helper(b, d)
      else if (d == b) d
      else helper(d, b)
    }

    val a1 = a.abs
    val b1 = b.abs

    val powerOfTwoDividingA = lowestSetBit(a1)
    val powerOfTwoDividingB = lowestSetBit(b1)

    val (b2, a2) = Orderer.ordered(
      a1 >> powerOfTwoDividingA,
      b1 >> powerOfTwoDividingB
    )

    val common = math.min(powerOfTwoDividingA, powerOfTwoDividingB)

    (1 << common) * helper(a2, b2)
  }

  def lowestSetBit(n: Int): Int = {

    ???
  }

  final class Two[+A] private(private val p: (A, A)) extends AnyVal {
    def _1: A = p._1
    def _2: A = p._2

    def swap: Two[A] = new Two(p.swap)

    def map[B](f: A => B): Two[B] = Two(f(_1), f(_2))

    override def toString: String = p.toString()

    implicit def toPair: (A, A) = p
  }
  object Two {
    implicit def fromPair[A](p: (A, A)): Two[A] = new Two(p)

    def apply[A](a1: A, a2: A): Two[A] = new Two(a1, a2)

    def unapply[A](two: Two[A]): Option[(A, A)] = Option(two.p)
  }
}
