package core.syntax

object Unit extends Val

sealed trait BoolVal extends Val
object True extends BoolVal
object False extends BoolVal
