package core.syntax

final case class U() extends Val

sealed trait BoolVal extends Val
object True extends BoolVal
object False extends BoolVal
