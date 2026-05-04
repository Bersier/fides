package fides.syntax.hierarchies

import fides.syntax.util.Hierarchy
import util.collections.extensional.FiniteSet
import util.collections.generic.SimpleSet
import util.{Trit}

def mirrored(hierarchy: Hierarchy): Hierarchy = new Hierarchy:
  type Element

  def u(elements: FiniteSet.NonEmpty[Element]): Element = ???
  def top: Element = ???

  def sign(element: Element): Trit = ???

  extension (element: Element)
    def <=(other: Element): Boolean = ???
