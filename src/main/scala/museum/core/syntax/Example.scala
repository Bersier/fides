package museum.core.syntax

object Example {
  val Alice: L[Nothing, Loc[A]] = new LocKey
  val Bob  : L[Nothing, Loc[A]] = new LocKey
  val code : L[Nothing, Prs]    = Forward(ScalaVal("Hi Alice!"), Alice)

//  private val bag: Bag[Nothing, A, Out] = Bag(Alice: L[Nothing, Out[A]], Bob: L[Nothing, Out[A]])
//  // L[Nothing, Out[Bag[_, A, Out]]]
//  private val bog: L[Nothing, Out[Bag[_, A, Out]]] = bag
//  val code2: L[Nothing, Prs] = Forward(ScalaVal("Hi All!"), Copy(bog))
}
