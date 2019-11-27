package doodle

object p2 extends App {
//
//  val regex: Regex = "=Sum\(([^,]+)+,\)".r
//  var groupRegex: Regex = "([A-Z]+[0-9]+)(\:[A-Z][0-9])?".r
//
//  def sum(formula: String, table: Array[Array[Int]]): Int = {
//    val groups: Regex.Match = regex.findFirstMatchIn(formula).get
//    var s = 0
//    for (group <- groups) {
//      val subgroups = groupRegex.findFirstMatchIn(group).get
//      if (subgroups.size == 1) {
//        val (x, y) = toCellLocation(subgroups(0))
//        s += table(x)(y)
//      }
//      else {
//        val (x1, y1) = toCellLocation(subgroups(0))
//        val (x2, y2) = toCellLocation(subgroups(1))
//        for (i <- x1 to x2) {
//          for (j <- y1 to y2) {
//            s += table(i)(j)
//          }
//        }
//      }
//    }
//    s
//  }
//
//  def toCellLocation(name: String): (Int, Int) = {
//    ???
//  }
//
//  def toNumber(letters: String): Int = {
//    var s = 0
//    var p = 1
//    for (letter <- letters.reverse) {
//      val i = letter.toInt - "A".toInt
//      s += i*p
//      p *= 26
//    }
//    s
//  }
}
