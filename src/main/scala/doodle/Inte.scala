package doodle

object Inte extends App {

  for (i <- 0 until 10) {
    println(solution(i))
  }

  def solution(n: Int): (Int, Int) = {
    up(0, 0, 1, n)
  }

  def up(x: Int, y: Int, n: Int, m: Int): (Int, Int) = {
    if (m <= n) (x, y + m)
    else right(x, y + n, n, m - n)
  }

  def right(x: Int, y: Int, n: Int, m: Int): (Int, Int) = {
    if (m <= n) (x + m, y)
    else down(x + n, y, n + 1, m - n)
  }

  def down(x: Int, y: Int, n: Int, m: Int): (Int, Int) = {
    if (m <= n) (x, y - m)
    else left(x, y - n, n, m - n)
  }

  def left(x: Int, y: Int, n: Int, m: Int): (Int, Int) = {
    if (m <= n) (x - m, y)
    else up(x - n, y, n + 1, m - n)
  }
}
