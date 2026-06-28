package fides.syntax.constructors.code

sealed trait Variance extends annotation.StaticAnnotation
object Variance:
  final case class Co() extends Variance
  final case class Contra() extends Variance
  final case class In() extends Variance
end Variance
