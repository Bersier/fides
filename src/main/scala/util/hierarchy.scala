package util

/**
  * [[<=]] and [[ElementT]] define a complete join-semilattice.
  */
trait Hierarchy:
  type ElementT

  def u(elements: Set[ElementT]): ElementT // todo NonEmptySet
  def top: ElementT
  
  extension (element: ElementT)
    def <=(other: ElementT): Boolean
end Hierarchy
