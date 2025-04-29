package fides.syntax.identifiers

import fides.syntax.core.Code
import fides.syntax.types.{CodeType, ValTop}

final class Name[T <: ValTop] private extends CodeType, Code[Name[T]]
//object Name:
//  def apply[T <: ValTop]()(using Context): Name[T] = Named.from(new Name(_))
//  def apply[T <: ValTop](name: String)(using Context): Name[T] = Named.from(new Name(_), name)
//end Name
