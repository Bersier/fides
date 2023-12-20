package fides2024.syntax

trait Component(using SyntaxSeal)
trait Expr[+P <: Polarity, +T <: ValSup](using SyntaxSeal) extends Component
sealed trait ValSup
trait Val[+T <: ValSup](using SyntaxSeal) extends Expr[Neutral, T], ValSup

sealed trait Polarity
sealed trait Positive extends Polarity
sealed trait Negative extends Polarity
sealed trait Neutral extends Positive, Negative

type Opposite[P <: Polarity] = P match
  case Positive => Negative
  case Negative => Positive
  case Neutral => Neutral
  case Opposite[p] => p

private[syntax] sealed trait SyntaxSeal
private[syntax] given SyntaxSeal with {}
