package doodle.graph

import doodle.graph.Utils.hash

import scala.collection.mutable
import scala.util.hashing.MurmurHash3

@specialized(Unit)
trait Graph[+V, +E] {
  def vertices: Set[Vertex]

  def map[NewV, NewE](morphism: (Vertex => NewV, (Vertex, E, Vertex) => NewE)): Graph[NewV, NewE]

  override def toString: String = {
    def f(p: (Vertex, E)): (String, E) = (p._1.toSmallString, p._2)
    val builder: mutable.StringBuilder = new StringBuilder
    builder.append("Graph {\n")
    for (vertex <- vertices) {
      builder.append("  ").append(vertex)
      builder.append(" -> ").append(vertex.neighbors.map[String, E](f)).append("\n")
    }
    builder.append("}")
    builder.toString
  }

  private val hashSeed = "Graph".hashCode

  override lazy val hashCode: Int = {
    if (vertices.isEmpty) {
      MurmurHash3.unorderedHash(Iterable.empty, hashSeed)
    }
    else {
      def initialVertexMap(vertex: Graph[V, E]#Vertex): Long = hash(vertex.value)
      def edgeMap(source: Any, e: Any, target: Any): Long = hash(e)
      def vertexMap(vertex: Graph[Long, Long]#Vertex): Long = {
        val neighborHash = {
          def f(p: (Graph[Long, Long]#Vertex, Long)): (Long, Long) = (p._1.value, p._2)
          hash(vertex.neighbors.map[Long, Long](f))
        }
        hash((vertex.value, neighborHash))
      }

      @scala.annotation.tailrec
      def loop(graph: Graph[Long, Long], counter: Int): Int = {
        if (counter == 0) MurmurHash3.unorderedHash(vertices.map(_.value), hashSeed)
        else loop(graph.map(vertexMap, edgeMap), counter - 1)
      }

      val initialGraph: Graph[Long, Long] = new SingleUseGraph(this).map(initialVertexMap, edgeMap)
      loop(initialGraph, initialGraph.vertices.size)
    }
  }

  override def equals(obj: Any): Boolean = obj match {
    case that: Graph[V, E] => Utils.isomorphic(this, that)
    case _ => false
  }

  trait Vertex {
    def neighbors: Map[Vertex, E]

    def value: V

    override def toString: String = {
      val builder: mutable.StringBuilder = new StringBuilder
      builder.append(toSmallString).append("(").append(value).append(")")
      builder.toString
    }

    private[Graph] def toSmallString = hashCode.toString
  }
}

object Graph {
  def apply(adjacencyList: Seq[Seq[Int]]): Graph[Unit, Unit] = {
    def intToTuple(i: Int): (Int, Unit) = {
      require(0 <= i)
      require(i < adjacencyList.size)
      (i, ())
    }
    GraphImpl(adjacencyList.map(s => ((), s.map(intToTuple))))
  }
}
