package doodle.graph

import scala.collection.mutable

/**
  * Becomes invalid as soon as map is invoked on it.
  */
private[graph] final class SingleUseGraph[+V, +E](template: Graph[V, E]) extends Graph[V, E] {
  private[this] val edges: Map[VertexImpl, mutable.Map[Vertex, Any]] = {
    val edges = mutable.Map.empty[VertexImpl, mutable.Map[Vertex, Any]]
    val corresponding = mutable.Map.empty[template.Vertex, VertexImpl]
    for (vertex <- template.vertices) {
      corresponding(vertex) = new VertexImpl(vertex.value)
    }
    for (vertex <- template.vertices) {
      val newNeighbors = mutable.Map.empty[Vertex, Any]
      for ((neighbor, e) <- vertex.neighbors) {
        newNeighbors(corresponding(neighbor)) = e
      }
      val newVertex = corresponding(vertex)
      edges(newVertex) = newNeighbors
    }
    edges.toMap
  }

  override def vertices: Set[Vertex] = edges.keySet.asInstanceOf[Set[Vertex]]

  override def map[NewV, NewE](morphism: (Vertex => NewV, (Vertex, E, Vertex) => NewE)): Graph[NewV, NewE] = {
    for ((vertex, neighbors) <- edges) {
      vertex() = morphism._1(vertex)
      neighbors.mapValuesInPlace((v, e) => morphism._2(vertex, e.asInstanceOf[E], v))
    }
    this.asInstanceOf[Graph[NewV, NewE]]
  }

  private[this] class VertexImpl(private[this] var v: Any) extends Vertex {
    override def neighbors: Map[Vertex, E] = edges(this).toMap.asInstanceOf[Map[Vertex, E]]

    override def value: V = v.asInstanceOf[V]

    def update(value: Any): Unit = {
      v = value
    }
  }
}
