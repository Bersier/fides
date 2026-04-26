package fides.syntax.hierarchies

import util.Trit

def mirrored(hierarchy: Hierarchy): Hierarchy = new Hierarchy:
  type ElementT
  
  def elements: Set[ElementT] = ???
  
  def u(elements: Set[ElementT]): ElementT = ???
  def top: ElementT = ???

  def sign(element: ElementT): Trit = ???
  
  extension (element: ElementT)
    def <=(other: ElementT): Boolean = ???  
