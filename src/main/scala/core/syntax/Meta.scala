package core.syntax

final case class Code[+K <: N, C[_ <: A] <: X, C2 <: Sort](code: L[CodeK[K, C], _ <: C2])
  extends A with L[K, C[Code[_, C, C2]]]
// What about a piece of code for a bag? How does it get evaluated? Need some kind of eval?
// Maybe move to Quote after all?

// A[+T] means S <: T => A[S] <: A[T]
// A[-T] means S >: T => A[S] <: A[T]

/**
  * Escapes one level of code.
  */
final case class Escape[+K <: N, C[_ <: A] <: X, C2 <: Sort](expr: L[K, _ <: C[Code[_, C, _ <: C2]]])
  extends L[CodeK[K, C], C2]

/**
  * Matches Escape when level is zero. Otherwise, matches a MatchEscape of a level lower by one.
  *
  * Note: one would need a more advanced type system than what Scala has to fully type higher-order
  * (i.e. where level > 0) MatchEscape.
  */
final case class MatchEscape[K <: N, C[_ <: A] <: X, C2 <: Sort](
  level: L[CodeK[K, Out], Val[Z]],
  expr : L[CodeK[K, Out], C[Code[CodeK[K, Out], C, C2]]]
) extends L[CodeK[CodeK[K, Out], C], C2]
// Not sure about these type parameters...

/*
Code[Nothing, Val[Code[Nothing, ]]](Code(Escape[](Loc[T]())))
 */

final case class Annotated[+K <: N, C <: Sort](l: L[K, C], annotation: L[K, Val[_]]) extends L[K, C]

// It looks like we could actually move back to a flow syntax. Perhaps do that later once it's stable, as a form that
// directly includes semantics.
