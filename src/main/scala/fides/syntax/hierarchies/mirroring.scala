package fides.syntax.hierarchies

import util.Hierarchy

def mirrored(hierarchy: Hierarchy): Hierarchy = new Hierarchy:
  type ElementT
  
  def u(elements: Set[ElementT]): ElementT = ???
  def top: ElementT = ???

  extension (element: ElementT)
    def <=(other: ElementT): Boolean = ???  
