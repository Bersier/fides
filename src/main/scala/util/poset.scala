package util

trait PosetOps[P]:
  extension (element: P)
    def <=>(otherElement: P): Trit
end PosetOps
