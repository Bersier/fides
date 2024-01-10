package fides2024.syntax.values

import fides2024.syntax.kinds.ValQ

/**
  * Boolean values
  */
sealed trait Bool extends ValQ[Bool]
case object True extends Bool
case object False extends Bool
