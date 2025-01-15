package util

sealed trait &:&[A, B]
object `&:&`:
  given [A, B] => (a: A, b: B) => (A &:& B)()
end `&:&`
