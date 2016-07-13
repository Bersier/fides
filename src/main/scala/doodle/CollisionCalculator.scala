package doodle

object CollisionCalculator extends App {

  println(computeCollisionCount(keyCount = 50000, arraySize = 1000000))

  def computeCollisionCount(keyCount: Int, arraySize: Int): Double = {
    keyCount - arraySize * (1 - math.pow(1 - 1.toDouble/arraySize, keyCount))
  }
}
