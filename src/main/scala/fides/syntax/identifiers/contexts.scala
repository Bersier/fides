package fides.syntax.identifiers

import scala.collection.concurrent

// todo
trait Context:
  def prefix: String
  val names: concurrent.Map[String, Identifier] // todo garbage-collect?
end Context
