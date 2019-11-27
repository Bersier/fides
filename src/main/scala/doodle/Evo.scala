package doodle

object Evo extends App {

  println("Evo")

  def evolveToFitness[Genome](population: Seq[(Genome, Double)])
                             (implicit target: Double,
                              eval: Genome => Double,
                              epsilon: Double,
                              mutator: Genome => Genome): Seq[(Genome, Double)] = {
    val worstAcceptableOffspringFitness = population.map(_._2).min + epsilon
    val offspring = population map (_._1) map mutator
    val offspringFitness = offspring map eval
    val viableOffspring = offspring.zip(offspringFitness).filter(_._2 >= worstAcceptableOffspringFitness)
    val champions = viableOffspring.filter(_._2 >= target)
    if (champions.nonEmpty) champions
    else evolveToFitness(viableOffspring ++ population.view.sortBy(-_._2).take(population.size - viableOffspring.size))
  }

  type Target[Genome] = Seq[(Genome, Double)] => Option[Target[Genome]] // Illegal cyclic reference.

  def evolveToFitness2[Genome](population: Seq[(Genome, Double)], target: Target[Genome])
                              (implicit eval: Genome => Seq[(Genome, Double)],
                               epsilon: Double,
                               mutator: Seq[Genome] => Seq[Genome]): Seq[(Genome, Double)] = {
    ???
  }

  type Eval[A] = A => Double
  type Eval2[A, B] = B => Eval[A]
}
