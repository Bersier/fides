package doodle.Collatz

object Ultron extends App {

  println("Computing Collatzer...")
  val collatzer = new Collatzer(14)
  println("Collatzer computed.")

  val e = 32
  var m = 1
  while (true) {
    val start = java.lang.System.currentTimeMillis()
    checkCollatz(m, e)(collatzer)
    val end = java.lang.System.currentTimeMillis()
    m += 1
    println("n = " + m * (1L << e) + " (" + (1L << e) + " steps computed in " + (end - start)/1000 + " seconds.)")
  }

  def checkCollatz(m: Int, e1: Int, de: Int)(implicit collatzer: Collatzer): Boolean = {
    val finalA = 1L << e1
    val dc = 1 << de
    val start = dc * m
    def helper(initialA: Long, initialB: Long, a: Long, b: Long): Boolean = {
      if (initialA == finalA) (start until (start + dc)) forall {
        i => collatzer.holdsFor(a * i + b)(initialA * i + initialB)
      }
      else if (a < initialA) true
      else if (a % 2 == 1) {
        helper(initialA << 1, initialB, a << 1, b) &&
        helper(initialA << 1, initialB + initialA, a << 1, b + a)
      }
      else if (b % 2 == 0) helper(initialA, initialB, a >> 1, b >> 1)
      else helper(initialA, initialB, 3 * a >> 1, (3 * b + 1) >> 1)
    }
    helper(2, 1, 3, 2)
  }

  def checkCollatz(m: Int, finalE: Int)(implicit collatzer: Collatzer): Boolean = {
    val finalA = 1L << finalE
    def helper(initialA: Long, initialB: Long, a: Long, b: Long): Boolean = {
      if (a < initialA) true
      else if (initialA == finalA) collatzer.holdsFor(a * m + b)(initialA * m + initialB)
      else if (a % 2 == 1) {
        helper(initialA << 1, initialB, a << 1, b) &&
        helper(initialA << 1, initialB + initialA, a << 1, b + a)
      }
      else if (b % 2 == 0) helper(initialA, initialB, a >> 1, b >> 1)
      else helper(initialA, initialB, 3 * a >> 1, (3 * b + 1) >> 1)
    }
    helper(2, 1, 3, 2)
  }
}