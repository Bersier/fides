package doodle

import scala.util.Random

object RandomNameGenerator extends App {

  val consonants = Array('b','c','d','f','g','h','j','k','l','m','n','p','q','r','s','t','v','w','x','z')
  val vowels = Array('a','e','i','o','u','y')

  def randomNameC(length: Int): String = {
    if (length == 0) ""
    else consonants(Random.nextInt(consonants.size)) + randomNameV(length - 1)
  }

  def randomNameV(length: Int): String = {
    if (length == 0) ""
    else vowels(Random.nextInt(vowels.size)) + randomNameC(length - 1)
  }

  while (true) {
    for (i <- 0 until 10) {
      for (j <- 0 until 10) {
        print(randomNameV(7) + " ")
      }
      println()
    }
    //Thread.sleep(1000)
  }

}
