package core.syntax

final case class Code[+K <: N, +C[_ <: A] <: X, C2 <: Sort](code: L[CodeK[K, C], _ <: C2])
  extends A with L[K, C[Code[_, D, C2]]]

final case class Quoted()

final case class Quote()
// What about a piece of code for a bag? How does it get evaluated? Need some kind of eval?
// Maybe move to Quote after all?
// Eval
// AsCode

/*
eval(X) = V
eval(Quoted(X)) = X
eval(Quoted(Q(Escape(X)) = Q(V)

Eval(X) = Quoted(V)
Eval(Quoted(X)) = Quoted(X)
Eval(Quoted(Q(Escape(X))) = Quoted(Q(V))
Eval(X(Quoted(Y)) = Quoted(V(Y))

type of Quoted(bool) should be bool, so
type of Quoted(proc) should be proc.
Really?

However, we don't want to allow Pair(       proc,         proc),
while    we       want to allow Pair(Quoted(proc), Quoted(proc)).

How to achieve this with Scala's type system?
Scala <-> Alax, Alacs

ExpandCode(V(Code(X)) => Code(V(X)) // What if V(X) is not legal?

// Perhaps static typing is not possible after all?
 */

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
