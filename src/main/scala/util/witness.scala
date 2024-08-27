package util

sealed trait &:&[A, B]
object `&:&`:
  given [A, B](using A, B): (A &:& B)()
end `&:&`
