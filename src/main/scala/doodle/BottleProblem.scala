package doodle

import scala.collection.mutable

object BottleProblem extends App {
  println(gcd(58739484, -549393848))
  println(gcdX(58739484, -549393848))
  println(BigInt(58739484).gcd(-549393848))
//  printWaterQuantitySteps(5, 3, 1)

  def printWaterQuantitySteps(capacity1: Int, capacity2: Int, desiredQuantity: Int): Unit = {
    require(capacity1 >= 0)
    require(capacity2 >= 0)
    require(desiredQuantity >= 0)
    val seen = mutable.Set.empty[(Int, Int)]
    def helper(q1: Int, q2: Int, acc: List[String]): Unit = if (! seen((q1, q2))) {
      val newAcc = ("(" + q1 + ", " + q2 + ")") :: acc
      assert(q1 >= 0)
      assert(q2 >= 0)
      if (q1 == desiredQuantity) {
        println(("Done: the first bottle now contains " + desiredQuantity + "." :: newAcc).reverse.mkString("\n"))
        System.exit(0)
      }
      else if (q2 == desiredQuantity) {
        println(("Done: the second bottle now contains " + desiredQuantity + "." :: newAcc).reverse.mkString("\n"))
        System.exit(0)
      }
      else {
        seen.add(q1, q2)
        helper(0, q2, "Empty the first bottle." :: newAcc)
        helper(q1, 0, "Empty the second bottle." :: newAcc);
        {
          val diff = math.min(capacity2 - q2, q1)
          helper(q1 - diff, q2 + diff, "Pour as much as possible from the first to the second bottle." :: newAcc)
        }
        {
          val diff = math.min(capacity1 - q1, q2)
          helper(q1 + diff, q2 - diff, "Pour as much as possible from the second to the first bottle." :: newAcc)
        }
        helper (capacity1, q2, "Fill the first bottle." :: newAcc)
        helper (q1, capacity2, "Fill the second bottle." :: newAcc)
      }
    }
    helper(0, 0, Nil)
    println("Impossible.")
  }

  def gcd(a: BigInt, b: BigInt): BigInt = {
    def helper(a: BigInt, b: BigInt): BigInt = {
      if (b == 0) a
      else helper(b, a % b)
    }
    if (b < 0) gcd(a, -b)
    else if (a < b) gcd(b, a)
    else helper(a, b)
  }

  def gcdX(a: BigInt, b: BigInt): BigInt = {
    def helper(a: BigInt, b: BigInt): BigInt = {
      val c = a - b
      val d = c >> c.lowestSetBit
      if (d < b) helper(b, d)
      else if (d == b) d
      else helper(d, b)
    }

    val a1 = a.abs
    val b1 = b.abs

    val powerOfTwoDividingA = a1.lowestSetBit
    val powerOfTwoDividingB = b1.lowestSetBit

    val (b2, a2) = Orderer.ordered(
      a1 >> powerOfTwoDividingA,
      b1 >> powerOfTwoDividingB
    )

    val common = math.min(powerOfTwoDividingA, powerOfTwoDividingB)

    (1 << common) * helper(a2, b2)
  }
}
