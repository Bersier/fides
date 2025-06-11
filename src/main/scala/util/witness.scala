package util

sealed trait &:&[A, B]
object `&:&`:
  given [A, B] => (A, B) => (A &:& B)()
end `&:&`
