package doodle

object AlternativeOperators extends App {

  def op(n: Double)(x: Double, y: Double): Double = {
    math.exp(math.pow(math.pow(math.log(x), n) + math.pow(math.log(y), n), 1/n))
  }

  def op(x: Double, y: Double): Double = {
    val logx: Double = math.log(x)
    val logy: Double = math.log(y)
    val arg: Double = math.abs(logx) * logx + math.abs(logy) * logy
    math.exp(math.signum(arg)*math.sqrt(math.abs(arg)))
  }

  for (n <- Seq(1, 2, 3)) {
    println("\nn = " + n)
    for (x <- 1 to 10) {
      for (y <- 1 to 10) {
        val z = op(n)(x/5.0, y/5.0)
        print(f"$z%05.3f" + " ")
      }
      println()
    }
  }

  println("\n\n")
  for (x <- Seq(1.0/5, 1.0/4, 1.0/3, 1.0/2, 1, 2, 3, 4, 5)) {
    for (y <- Seq(1.0/5, 1.0/4, 1.0/3, 1.0/2, 1, 2, 3, 4, 5)) {
      val z = op(x, y)
      print(f"$z%05.3f" + " ")
    }
    println()
  }
}
