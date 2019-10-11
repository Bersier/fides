package doodle

object Counter extends App {

  def foo(n: Int): Int = n * n + 1

  for (i <- 1 to 5) {
    val start: Long = System.currentTimeMillis()
    val a: Int = repeat0(1000000000)(foo)(0)
    val end: Long = System.currentTimeMillis()
    println("answer: " + a + "; time: " + (end - start))
  }

  println("-------------")

  for (i <- 1 to 5) {
    val start: Long = System.currentTimeMillis()
    val a: Int = repeat1(1000000000)(foo)(0)
    val end: Long = System.currentTimeMillis()
    println("answer: " + a + "; time: " + (end - start))
  }

  def repeat0(n: Int)(f: Int => Int)(a: Int): Int = {
    require(n >= 0)
    @scala.annotation.tailrec
    def helper(n: Int, a: Int): Int = {
      if (n == 0) a
      else helper(n - 1, f(a))
    }
    helper(n, a)
  }

  def repeat1(n: Int)(f: Int => Int)(a: Int): Int = {
    require(n >= 0)
    @scala.annotation.tailrec
    def helper(n: Int, a: Int): Int = {
      if (n < 4) n match {
        case 0 => a
        case 1 => f(a)
        case 2 => f(f(a))
        case 3 => f(f(f(a)))
      }
      else helper(n - 4, f(f(f(f(a)))))
    }
    helper(n, a)
  }
}
