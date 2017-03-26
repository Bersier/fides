package doodle.Collatz

final class Collatzer(e: Int) {
  require(e < (1 << 5) - 1)
  private[this] val c: Int = 1 << e
  private[this] val fallBackIn: Int = 1 << 16
  private[this] val mem: Array[Long] = Array.ofDim(c << 1)

  compute(c)

  def holdsFor(n: BigInt)(implicit initialN: BigInt = n): Boolean = {
    def shrinksForFast(n: BigInt, fallBackIn: Int): Boolean = {
      if (fallBackIn <= 0) {
        println("n = " + initialN + " failed to glide down.")
        shrinksFor(n, n)
      }
      else {
        val next = nextN(n)
        if (next < initialN) true
        else shrinksForFast(next, fallBackIn - 1)
      }
    }
    def shrinksFor(n1: BigInt, n2: BigInt): Boolean = {
      val afterN2 = nextN(n2)
      val nextN2 = nextN(afterN2)
      val nextN1 = nextN(n1)
      if (nextN2 < initialN || afterN2 < initialN) true
      else if (nextN1 == nextN2) {
        println("Loop detected at n = " + initialN + ".")
        false
      }
      else shrinksFor(nextN1, nextN2)
    }
    if (initialN % 3 == 2 || initialN % 9 == 4) true
    else shrinksForFast(n, fallBackIn)
  }

  @inline private[this] def nextN(n: BigInt): BigInt = {
    val ii = (n % c).toInt << 1
    mem(ii) * (n >> e) + mem(ii + 1)
  }

  private[this] def compute(finalA: Int): AddToMem.type = {
    def compute(initialA: Int, initialB: Int, a: Long, b: Long, sa: Long, sb: Long): AddToMem.type = {
      if (a % 2 == 1) {
        if (initialA == finalA) {
          if (sa < initialA) addToMem(initialB, sa, sb)
          else addToMem(initialB, a, b)
        }
        else {
          compute(initialA << 1, initialB, a << 1, b, sa << 1, sb)
          compute(initialA << 1, initialB + initialA, a << 1, b + a, sa << 1, sb + sa)
        }
      }
      else if (b % 2 == 0) {
        val nextA = a >> 1
        val nextB = b >> 1
        if (nextA < sa) compute(initialA, initialB, nextA, nextB, nextA, nextB)
        else compute(initialA, initialB, nextA, nextB, sa, sb)
      }
      else compute(initialA, initialB, 3 * a >> 1, (3 * b + 1) >> 1, sa, sb)
    }
    compute(1, 0, 1, 0, 1, 0)
  }

  private[this] def addToMem(i: Int, a: Long, b: Long): AddToMem.type = {
    val ii = i << 1
    mem(ii) = a
    mem(ii + 1) = b
    AddToMem
  }

  private[this] object AddToMem
}

//  private[this] def compute(initialA: Long, initialB: Long): (Long, Long) = {
//    def compute(a: Long, b: Long, sa: Long, sb: Long): (Long, Long) = {
//      if (a % 2 == 1) {
//        if (sa == initialA) (a, b)
//        else (sa, sb)
//      }
//      else if (b % 2 == 0) {
//        val nextA = a >> 1
//        val nextB = b >> 1
//        if (nextA < sa) compute(nextA, nextB, nextA, nextB)
//        else compute(nextA, nextB, sa, sb)
//      }
//      else compute(3 * a >> 1, (3 * b + 1) >> 1, sa, sb)
//    }
//    compute(initialA, initialB, initialA, initialB)
//  }

//  def compute(finalA: Int): Unit = {
//    def compute(initialA: Int, initialB: Int, a: Long, b: Long): Unit = {
//      if (a % 2 == 1) {
//        if (initialA == finalA) addToMem(initialB, a, b)
//        else {
//          compute(initialA << 1, initialB, a << 1, b)
//          compute(initialA << 1, initialB + initialA, a << 1, b + a)
//        }
//      }
//      else if (b % 2 == 0) compute(initialA, initialB, a >> 1, b >> 1)
//      else compute(initialA, initialB, 3 * a >> 1, (3 * b + 1) >> 1)
//    }
//    compute(1, 0, 1, 0)
//  }