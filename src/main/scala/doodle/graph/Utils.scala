package doodle.graph

object Utils extends App {

  {
    val graph1 = Graph(Seq(Seq(1), Seq(2), Seq(0)))
    val graph2 = Graph(Seq(Seq(2), Seq(0), Seq(1)))
    println(isomorphic(graph1, graph2))
  }

  def isomorphic[V, E](graph1: Graph[V, E], graph2: Graph[V, E]): Boolean = {
    if      (graph1.vertices.isEmpty) graph2.vertices.isEmpty
    else if (graph2.vertices.isEmpty) graph1.vertices.isEmpty
    else {
      def initialVertexMap(vertex: Graph[V, E]#Vertex): Long = hash(vertex.value)
      def edgeMap(source: Any, e: Any, target: Any): Long = hash(e)
      def vertexMap(vertex: Graph[Long, Long]#Vertex): Long = {
        val neighborHash = {
          def f(p :(Graph[Long, Long]#Vertex, Long)): (Long, Long) = (p._1.value, p._2)
          hash(vertex.neighbors.map[Long, Long](f))
        }
        hash((vertex.value, neighborHash))
      }

      println("input graph1 = " + graph1)
      println("input graph2 = " + graph2)
      println("\n")

      val initialGraph1: Graph[Long, Long] = new SingleUseGraph(graph1).map(initialVertexMap, edgeMap)
      val initialGraph2: Graph[Long, Long] = new SingleUseGraph(graph2).map(initialVertexMap, edgeMap)

      @scala.annotation.tailrec
      def loop(graph1: Graph[Long, Long], graph2: Graph[Long, Long], counter: Int): Boolean = {
        println("graph1 = " + graph1)
        println("graph2 = " + graph2)
        println("\n")
        if (graph1.vertices.map(_.value) != graph2.vertices.map(_.value)) false
        else if (counter == 0) true
        else loop(graph1.map(vertexMap, edgeMap), graph2.map(vertexMap, edgeMap), counter - 1)
      }

      // TODO Number of iterations should be insufficient in worst case
      loop(initialGraph1, initialGraph2, math.max(initialGraph1.vertices.size, initialGraph2.vertices.size))
    }
  }

  private[graph] def hash(something: Any): Long = something.hashCode()
}
