package core.syntax

final case class U() extends V[AllK, U]

sealed trait BoolVal extends V[AllK, BoolVal]
object True extends BoolVal
object False extends BoolVal
