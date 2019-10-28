package doodle

object Triangle extends App {

  val level = -1
  private val repeatedExp: Int => Double => Double = repeated(math.exp, math.log)
  var current = Array.fill[Double](1)(repeatedExp(level)(1))
  for (i <- 0 to 9) {
    println(current.toList)
    current = nextRow(level)(current)
  }

  def nextRow(level: Int)(row: Array[Double]): Array[Double] = {
    val result = Array.ofDim[Double](row.length + 1)
    def loop(prev: Double, i: Int): Unit = if (i < row.length) {
      result(i) = op(level)(prev, row(i))
      loop(row(i), i + 1)
    }
    val identity = repeatedExp(level)(0)
    loop(identity, 0)
    result(row.length) = op(level)(row(row.length - 1), identity)
    result
  }

  def op(level: Int)(left: Double, right: Double): Double = {
    val f: Int => Double => Double = repeatedExp
    f(level)(f(-level)(left) + f(-level)(right))
  }

  def repeated[A](f: A => A, fInv: A => A)(n: Int)(x: A): A = {
    def repeated(n: Int, x: A)(implicit f: A => A): A = {
      if (n == 0) x
      else repeated(n - 1, f(x))
    }
    if (n < 0) repeated(-n, x)(fInv)
    else repeated(n, x)(f)
  }
}
