package core.syntax

sealed trait BoolVal extends Val
object True extends BoolVal
object False extends BoolVal
