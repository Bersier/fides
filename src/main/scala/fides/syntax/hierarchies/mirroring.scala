package fides.syntax.hierarchies

import fides.syntax.util.Hierarchy
import util.Trit
import util.collections.extensional.FiniteSet
import util.collections.generic.SimpleSet

def mirrored(hierarchy: Hierarchy): Hierarchy = new Hierarchy:
  type ElementT
  
  def elements: SimpleSet[ElementT] = ???
  
  def u(elements: FiniteSet.NonEmpty[ElementT]): ElementT = ???
  def top: ElementT = ???

  def sign(element: ElementT): Trit = ???
  
  extension (element: ElementT)
    def <=(other: ElementT): Boolean = ???  
