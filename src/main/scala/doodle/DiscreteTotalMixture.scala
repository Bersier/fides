package doodle

object DiscreteTotalMixture extends App {

  for (i <- 0 to 13) {
    val r = row(pointCount = i, radius = i)
    println(i + ": " + r.sum + " " + r.toList)
  }

  def row(pointCount: Int, radius: Int): Array[Long] = {
    val histogram = Array.ofDim[Long](2*radius + 1)

    if (pointCount > 0) {
      rem(0, Nil, pointCount, radius)
    }

    def rem(start: Int, positions: List[Int], pointCount: Int, distance: Int) {
//      require (pointCount >= 1)
//      require (distance >= 0)
//      require (start < histogram.length)

      val startDistance = math.abs(start - radius)
      if (startDistance <= distance) {
        val newPositions = start :: positions
        if (pointCount == 1) {
          for (p <- newPositions) {
            histogram(p) += 1
          }
        }
        else {
          rem(start, newPositions, pointCount - 1, distance - startDistance)
        }

        val newStart = start + 1
        if (newStart < histogram.length) {
          rem(newStart, positions, pointCount, distance)
        }
      }
      else if (start < radius) {
        rem(start + 1, positions, pointCount, distance)
      }
    }

    histogram
  }
}
