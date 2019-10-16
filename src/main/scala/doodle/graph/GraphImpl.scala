package doodle.graph

import scala.collection.immutable
import scala.reflect.ClassTag

private[graph] final class GraphImpl[+V, +E] private(private[this] val values: Seq[V],
                                                     private[this] val edges: Seq[Map[Int, E]]) extends Graph[V, E] {

  override val vertices: Set[Vertex] = new Set[Vertex] {
    override def incl(vertex: Vertex): Set[Vertex] = trueSet.incl(vertex)
    override def excl(vertex: Vertex): Set[Vertex] = trueSet.excl(vertex)
    override def contains(vertex: Vertex): Boolean = true
    override def iterator: Iterator[Vertex] = values.indices.iterator.map(VertexImpl)
    private def trueSet: Set[Vertex] = values.indices.map(VertexImpl).toSet[Vertex]
  }

  override def map[NewV, NewE](morphism: (Vertex => NewV, (Vertex, E, Vertex) => NewE)): Graph[NewV, NewE] = {
    GraphImpl(this)(morphism)
  }

  private[this] case class VertexImpl(i: Int) extends Vertex {
    override def neighbors: Map[Vertex, E] = {
      new Map[Vertex, E] {
        override def removed(key: Vertex): Map[Vertex, E] = trueMap.removed(key)
        override def updated[V1 >: E](key: Vertex, value: V1): Map[Vertex, V1] = trueMap.updated(key, value)
        override def get(key: Vertex): Option[E] = edges(i).get(key.asInstanceOf[VertexImpl].i)
        override def iterator: Iterator[(Vertex, E)] = edges(i).iterator.map(f)
        private def f(p: (Int, E)): (Vertex, E) = (VertexImpl(p._1), p._2)
        private def trueMap: Map[Vertex, E] = edges(i).map(f)
      }
    }

    override def value: V = values(i)
  }
}

private[graph] object GraphImpl {
  private type EdgeMorphismT[V, E, NewE, VertexT] = (VertexT, E, VertexT) => NewE
  private type MorphismT[V, E, NewV, NewE, VertexT] = (VertexT => NewV, EdgeMorphismT[V, E, NewE, VertexT])

  def apply[V, E](adjacencyList: Seq[(V, Seq[(Int, E)])]): Graph[V, E] = {
    def f(p: (V, Seq[(Int, E)])): Boolean = p._2.size == p._2.map(_._1).toSet.size
    require(adjacencyList.forall(f))
    new GraphImpl(adjacencyList.map(_._1), adjacencyList.map(_._2.toMap))
  }

  private def apply[V, E, NewV, NewE](graph: Graph[V, E])
                                     (morphism: MorphismT[V, E, NewV, NewE, graph.Vertex]): GraphImpl[NewV, NewE] = {
    val values: Seq[NewV] = {
      if (graph.vertices.isEmpty) Seq()
      else {
        val classTag: ClassTag[NewV] = ClassTag(morphism._1(graph.vertices.iterator.next()).getClass)
        immutable.ArraySeq.unsafeWrapArray(
          graph.vertices.map(vertex => morphism._1(vertex)).toArray(classTag)
        )
      }
    }
    val edges: Seq[Map[Int, NewE]] = {
      val corresponding = graph.vertices.zipWithIndex.toMap
      val edges = Array.ofDim[Map[Int, NewE]](graph.vertices.size)
      for ((vertex, i) <- graph.vertices.zipWithIndex) {
        def f(p: (graph.Vertex, E)): (Int, NewE) = (corresponding(p._1), morphism._2(vertex, p._2, p._1))
        edges(i) = vertex.neighbors.map(f)
      }
      edges
    }
    new GraphImpl(values, edges)
  }
}
