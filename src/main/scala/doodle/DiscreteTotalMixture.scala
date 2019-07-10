package doodle

object DiscreteTotalMixture extends App {

  for (i <- 0 to 50) {
    val r = row(pointCount = i, radius = i)
    println(i + ": " + r.sum + " " + r.toList)
  }

  def row(pointCount: Int, radius: Int): Array[Long] = {
    require (pointCount >= 0)
    require (radius >= 0)

    val histogram = Array.ofDim[Long](2*radius + 1)

    if (pointCount > 0) {
      if (radius > 0) {
        rem0(start = 0, positions = Nil, pointCount, radius)
      }
      else {
        histogram(0) += pointCount
      }
    }

    def rem0(start: Int, positions: List[Int], pointCount: Int, distance: Int) {
//      require(pointCount >= 1)
//      require(distance >= 0)
//      require(start >= 0)
//      require(start < radius)

      val startDistance = radius - start
      if (startDistance <= distance) {
        val newPositions = start :: positions
        if (pointCount == 1) {
          addPoints(newPositions)
        }
        else {
          rem0(start, newPositions, pointCount - 1, distance - startDistance)
        }
      }
      val nextStart = start + 1
      if (nextStart == radius) {
        rem1(nextStart, positions, pointCount, distance)
      }
      else {
        rem0(nextStart, positions, pointCount, distance)
      }
    }

    def rem1(start: Int, positions: List[Int], pointCount: Int, distance: Int) {
//      require(pointCount >= 1)
//      require(distance >= 0)
//      require(start >= radius)
//      require(start < histogram.length)
//      require(pointCount * (start - radius) <= distance)

      val startDistance = start - radius
      val newPositions = start :: positions
      if (pointCount == 1) {
        addPoints(newPositions)
      }
      else {
        rem1(start, newPositions, pointCount - 1, distance - startDistance)
      }
      val nextStart = start + 1
      if (pointCount * (startDistance + 1) <= distance && nextStart < histogram.length) {
        rem1(nextStart, positions, pointCount, distance)
      }
    }

    def addPoints(positions: List[Int]) {
      for (p <- positions) {
        histogram(p) += 1
      }
    }

    histogram
  }
}
