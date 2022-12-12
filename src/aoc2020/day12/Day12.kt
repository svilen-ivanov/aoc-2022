package aoc2020.day12

import com.google.common.base.Stopwatch
import org.jetbrains.kotlinx.multik.api.d2array
import org.jetbrains.kotlinx.multik.api.mk
import org.jetbrains.kotlinx.multik.ndarray.data.*
import org.jetbrains.kotlinx.multik.ndarray.data.set
import readInput

data class Point(val x: Int, val y: Int) {
    operator fun plus(other: Point): Point {
        return Point(x + other.x, y + other.y)
    }

    private fun isOnMap(map: NDArray<Int, D2>): Boolean {
        return x >= 0 && y >= 0 && x < map.shape[0] && y < map.shape[1]
    }

    fun canMoveTo(nextPoint: Point, map: NDArray<Int, D2>): Boolean {
        return nextPoint.isOnMap(map) && (map[nextPoint.x, nextPoint.y] - map[x, y] <= 1)
    }

    fun toIndex(map: NDArray<Int, D2>): Int {
        return x * map.shape[1] + y
    }

    companion object {
        fun fromIndex(i: Int, map: NDArray<Int, D2>): Point {
            return Point(i / map.shape[1], i % map.shape[1])
        }
    }
}

val dirs = listOf(Point(0, 1), Point(0, -1), Point(1, 0), Point(-1, 0))

class PathFinder(
    private val map: NDArray<Int, D2>,
    private val starts: List<Point>,
    private val end: Point
) {

    private val graph: Graph = Graph(map.shape[0] * map.shape[1])

    init {
        for (y in 0 until map.shape[1]) {
            for (x in 0 until map.shape[0]) {
                val curr = Point(x, y)
                for (dir in dirs) {
                    val next = curr + dir
                    if (curr.canMoveTo(next, map)) {
                        val fromIndex = curr.toIndex(map)
                        val toIndex = next.toIndex(map)
                        graph.edges.add(Edge(fromIndex, toIndex, 1))
                    }
                }
            }
        }
    }


    fun shortestPath(): Int {
        val sw = Stopwatch.createStarted()
        val min = starts.parallelStream().map { start ->
//            println("${index}/${starts.size}")
            val from = start.toIndex(map)
            val distances = BellmanFord.getShortestPaths(graph, from)
            for (destIndex in distances.indices) {
                val dest = Point.fromIndex(destIndex, map)
                if (dest == end) {
//                        println("\t" + i + " " + "\t\t" + if (distances[i] == Int.MAX_VALUE) "-" else distances[i])
                    return@map distances[destIndex]
                }
            }
            error("No path found")
        }.toList().min()
        println("Time: ${sw.stop()}")
//        println("Min: ${min}")
        return min
    }

}

fun main() {
    fun part1(input: List<String>) {
        val width = input.first().length
        val height = input.size
        val map = mk.d2array(width, height) { 0 }
//        var start: Point? = null
        val starts = mutableListOf<Point>()

        var end: Point? = null
//        println(map)
        for ((y, line) in input.withIndex()) {
            for ((x, elevation) in line.withIndex()) {
                when (elevation) {
                    'S', 'a' -> {
                        starts.add(Point(x, y))
                        map[x, y] = 0
                    }

                    'E' -> {
                        end = Point(x, y)
                        map[x, y] = ('z' - 'a')
                    }

                    else -> map[x, y] = (elevation - 'a')
                }
            }
        }
        require(starts.isNotEmpty())
        requireNotNull(end)

        val path = PathFinder(map, starts, end).shortestPath()
        println(path)
//        println("-------------")
//        for (y in 0 until map.shape[1]) {
//            for (x in 0 until map.shape[0]) {
//                val p = Point(x, y)
//                val f = path.indexOf(p)
//                print("%02d (%2s) : ".format(map[x, y], if (f >= 0) f else "."))
//            }
//            println()
//        }
//        println("Shortest path is:\n${path.joinToString("\n")}")
//        println("Size: ${path.size - 1}")
    }

    fun part2(input: List<String>) {
    }

    val testInput = readInput("day12/day12")
    part1(testInput)
    part2(testInput)
}
