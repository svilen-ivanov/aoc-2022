package aoc2022.day12

data class Edge(val from: Int, val to: Int, val weight: Int)

class Graph(val vertCount: Int) {
    val edges = mutableListOf<Edge>()
}

object BellmanFord {
    fun getShortestPaths(graph: Graph, start: Int): IntArray {
        val distance = IntArray(graph.vertCount)

        for (i in 1 until graph.vertCount) {
            distance[i] = Int.MAX_VALUE
        }
        distance[start] = 0

        for (i in 1 until graph.vertCount) {
            for (edge in graph.edges) {
                val newDistance = distance[edge.from] + edge.weight
                if (distance[edge.from] != Int.MAX_VALUE && distance[edge.to] > newDistance) {
                    distance[edge.to] = newDistance
                }
            }
        }
        //checks if there exist negative cycles in graph G
        for (edge in graph.edges) {
            if (distance[edge.from] != Int.MAX_VALUE && distance[edge.to] > distance[edge.from] + edge.weight) {
                error("Negative cycle detected")
            }
        }
        return distance
    }
}



