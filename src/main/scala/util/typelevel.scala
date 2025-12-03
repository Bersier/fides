package util

sealed trait Bool

object Bool:
  type F = Bool
  final abstract class T extends Bool
end Bool

sealed trait NatN
final abstract class Zero extends NatN
final abstract class Succ[+N <: NatN] extends NatN
