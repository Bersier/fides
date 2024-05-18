package util

extension (b: Boolean)
  inline def asOption: Option[Unit] = if b then Some(()) else None
  inline infix def thenYield[T](inline t: => T): Option[T] = if b then Some(t) else None
  inline infix def thenFlatYield[T](inline o: => Option[T]): Option[T] = if b then o else None

extension [T](o: Option[T])
  inline def orElseIf(inline condition: Boolean)(inline alternative: => Option[T]): Option[T] =
    o orElse (condition thenFlatYield alternative)
  inline infix def orElse(inline alternative: => Option[T], inline provided: Boolean = true): Option[T] =
    o.orElseIf(provided)(alternative)

extension [T](s: Some[T])
  inline def unpacked: T = s.get
