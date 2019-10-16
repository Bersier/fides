package doodle

import scala.collection.mutable

object GraphIsomorphismChecker extends App {

  println(isomorphic(???, ???))

  def isomorphic[V, E](graph1: Graph[V, E], graph2: Graph[V, E]): Boolean = {
    if (graph1.vertices.size != graph2.vertices.size) false
    else if (graph1.vertices.isEmpty) true
    else {
      def initialVertexMap(vertex: Graph[V, E]#Vertex): Long = hash(vertex.value)
      def edgeMap(source: Any, e: Any, target: Any): Long = hash(e)
      def vertexMap(vertex: Graph[Long, Long]#Vertex): Long = hash((vertex.value, vertex.neighbors))

      val initialGraph1: Graph[Long, Long] = graph1.map(initialVertexMap, edgeMap)
      val initialGraph2: Graph[Long, Long] = graph2.map(initialVertexMap, edgeMap)

      @scala.annotation.tailrec
      def loop(graph1: Graph[Long, Long], graph2: Graph[Long, Long], counter: Int): Boolean = {
        if (counter == 0) {
          graph2.vertices.map(_.value).contains(graph1.vertices.iterator.next().value)
        }
        else loop(graph1.map(vertexMap, edgeMap), graph2.map(vertexMap, edgeMap), counter - 1)
      }

      loop(initialGraph1, initialGraph2, initialGraph1.vertices.size)
    }
  }

  def hash(something: Any): Long = something.hashCode()

  private final class SingleUseGraph[+V, +E](template: Graph[V, E]) extends Graph[V, E] {
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
        vertex() = morphism._1(vertex.value)
        neighbors.mapValuesInPlace((_, e) => morphism._2(e.asInstanceOf[E]))
      }
      this.asInstanceOf[Graph[NewV, NewE]]
    }

    private[this] final class VertexImpl(private[this] var v: Any) extends Vertex {
      override def neighbors: Map[Vertex, E] = edges(this).asInstanceOf[Map[Vertex, E]]

      override def value: V = v.asInstanceOf[V]

      def update(value: Any): Unit = {
        v = value
      }
    }
  }

  private final class GraphImpl[+V, +E](private[this] val edges: Seq[(V, Map[Int, E])]) extends Graph[V, E] {

    def this(adjacencyList: Seq[(V, Seq[(Int, E)])]) {
      this(adjacencyList.map[(V, Map[Int, E])](f[V, E]))
    }

    def this(graph: Graph[V, E]) {
      this(???)
    }

    override val vertices: Set[Vertex] = new Set[Vertex] {
      override def incl(elem: Vertex): Set[Vertex] = trueSet.incl(elem)

      override def excl(elem: Vertex): Set[Vertex] = trueSet.excl(elem)

      override def contains(elem: Vertex): Boolean = true

      override def iterator: Iterator[Vertex] = edges.indices.iterator.map(VertexImpl)

      private def trueSet: Set[Vertex] = edges.indices.map(VertexImpl).toSet[Vertex]
    }

    override def map[NewV, NewE](morphism: (Vertex => NewV, (Vertex, E, Vertex) => NewE)): Graph[NewV, NewE] = {

      ???
    }

    private[this] def f[A, B](p: (A, Seq[(Int, B)])): (A, Map[Int, B]) = (p._1, p._2.toMap)

    private[this] final case class VertexImpl(representation: Int) extends Vertex {
      override def neighbors: Map[Vertex, E] = {
        def f(p: (Int, E)): (Vertex, E) = (VertexImpl(p._1), p._2)
        edges(representation)._2.map(f)
      }

      override def value: V = edges(representation)._1
    }
  }

  trait Graph[+V, +E] {
    def vertices: Set[Vertex]

    def map[NewV, NewE](morphism: (Vertex => NewV, (Vertex, E, Vertex) => NewE)): Graph[NewV, NewE]

    trait Vertex {
      def neighbors: Map[Vertex, E]

      def value: V
    }
  }

  object Graph {
    def apply(adjacencyList: Seq[Seq[Int]]): Graph[Unit, Unit] = {
      val argument: Seq[(Unit, Seq[(Int, Unit)])] = adjacencyList.map(s => ((), s.map((_, ()))))
      new GraphImpl[Unit, Unit](argument)
    }
  }

  sealed trait Union[+A, +B]
}
