package doodle

object Interview extends App {

  println(4+5*6/3-7/24*5)
  println(solution("4+5*6/3-7/24*5"))

  def solution(expression: String): Int = {
    val (result, Seq()) = eval(expression.toList)(0)
    result
  }

  /**
    * Evaluates the given expression until it encounters an operator of lower precedence than the given one.
    * @return the result of the evaluation, and the unevaluated remainder
    */
  def eval(expression: Seq[Char])(implicit precedence: Int): (Int, Seq[Char]) = {
    val (first, tail) = splitOffFirstInt(expression)
    eval(first, tail)
  }

  def eval(first: Int, expression: Seq[Char])(implicit precedence: Int): (Int, Seq[Char]) = expression match {
    case Seq() => (first, Seq())
    case operator +: tail => eval(first, operator, tail)
  }

  def eval(left: Int, operator: Char, expression: Seq[Char])(implicit precedence: Int): (Int, Seq[Char]) = {
    if (precedenceOf(operator) < precedence) (left, operator +: expression)
    else {
      val (right, tail) = getRightAndTail(operator, expression)
      eval(evalOperation(left, operator, right), tail)
    }
  }

  def getRightAndTail(operator: Char, expression: Seq[Char]): (Int, Seq[Char]) = {
    eval(expression)(precedenceOf(operator) + 1)
  }

  def evalOperation(left: Int, operator: Char, right: Int): Int = operator match {
    case '+' => left + right
    case '-' => left - right
    case '*' => left * right
    case '/' => left / right
  }

  def precedenceOf(operator: Char): Int = operator match {
    case '+' => 0
    case '-' => 0
    case '*' => 1
    case '/' => 1
  }

  @scala.annotation.tailrec
  def splitOffFirstInt(expression: Seq[Char], acc: Int = 0): (Int, Seq[Char]) = expression match {
    case head +: tail if head.isDigit => splitOffFirstInt(tail, head.asDigit + 10 * acc)
    case _ => (acc, expression)
  }
}
