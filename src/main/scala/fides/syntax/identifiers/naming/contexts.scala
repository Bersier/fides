package fides.syntax.identifiers.naming

import fides.syntax.meta.Launcher

import scala.collection.concurrent

// todo
trait Context:
  def prefix: String
  final val names: concurrent.Map[String, Named] = concurrent.TrieMap("Launcher" -> Launcher)
  // todo garbage-collect?
end Context
