package fides2024.syntax.meta

import fides2024.syntax.kinds.{Code, CodeType, Expr}
import fides2024.syntax.signatures.Signed

final case class Launch[C <: CodeType](code: Code[Expr[Quoted[C]]]) extends Expr[Signed[Quoted[C]]]
