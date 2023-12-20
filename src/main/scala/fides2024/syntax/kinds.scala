package fides2024.syntax

trait Component(using SyntaxSeal)
trait Expr[P <: Polarity](using SyntaxSeal) extends Component
trait Val(using SyntaxSeal) extends Expr[Neutral]

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
