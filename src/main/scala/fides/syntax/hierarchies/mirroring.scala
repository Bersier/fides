package fides.syntax.hierarchies

import fides.syntax.util.Hierarchy
import util.{NonEmptyFiniteSet, SimpleSet, Trit}

def mirrored(hierarchy: Hierarchy): Hierarchy = new Hierarchy:
  type ElementT
  
  def elements: SimpleSet[ElementT] = ???
  
  def u(elements: NonEmptyFiniteSet[ElementT]): ElementT = ???
  def top: ElementT = ???

  def sign(element: ElementT): Trit = ???
  
  extension (element: ElementT)
    def <=(other: ElementT): Boolean = ???  
