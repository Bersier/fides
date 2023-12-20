package fides2024.syntax

trait Component(using SyntaxSeal)
trait Expr(using SyntaxSeal) extends Component // todo add polarity?
trait Val(using SyntaxSeal) extends Expr

private[syntax] class SyntaxSeal
private[syntax] given SyntaxSeal with {}
