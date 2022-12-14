package aoc2022.day14

import org.jetbrains.kotlinx.multik.api.d2array
import org.jetbrains.kotlinx.multik.api.mk
import org.jetbrains.kotlinx.multik.ndarray.data.*
import org.jetbrains.kotlinx.multik.ndarray.data.get
import readInput
import kotlin.math.max
import kotlin.math.min

enum class Outcome {
    FELL,
    SETTLED,
    ABYSS,
}

data class Point(var x: Int, var y: Int) {
    fun fall(cave: NDArray<Int, D2>): Outcome {
        var nextX = x
        val nextY = y + 1
        if (nextX >= cave.shape[0]) {
            return Outcome.ABYSS
        }
        if (!(nextY >= cave.shape[1])) {
            when (cave[nextX, nextY]) {
                AIR -> {
                    x = nextX
                    y = nextY
                    return Outcome.FELL
                }
            }
        }
        nextX = x - 1
        if (nextX < 0) {
            return Outcome.ABYSS
        }
        if (!(nextY >= cave.shape[1])) {

            when (cave[nextX, nextY]) {
                AIR -> {
                    x = nextX
                    y = nextY
                    return Outcome.FELL
                }
            }
        }
        nextX = x + 1
        if (nextX >= cave.shape[0]) {
            return Outcome.ABYSS
        }
        if (!(nextY >= cave.shape[1])) {
            when (cave[nextX, nextY]) {
                AIR -> {
                    x = nextX
                    y = nextY
                    return Outcome.FELL
                }
            }
        }
        return Outcome.SETTLED
    }
}

val ROCK = 1
val AIR = 0
val SAND = 2

fun main() {

    fun rockLine(cave: NDArray<Int, D2>, prevPoint: Point, point: Point) {
        for (dx in min(prevPoint.x, point.x)..max(prevPoint.x, point.x)) {
            for (dy in min(prevPoint.y, point.y)..max(prevPoint.y, point.y)) {
                cave[dx, dy] = ROCK
            }
        }
    }

    fun printCave(cave: NDArray<Int, D2>) {
        for (y in 0 until cave.shape[1]) {
            for (x in 480 until cave.shape[0]) {
                print(
                    when (cave[x, y]) {
                        ROCK -> "#"
                        SAND -> "o"
                        AIR -> "."
                        else -> error("")
                    }
                )
            }
            println()
        }
    }

    fun simulate(cave: NDArray<Int, D2>, point: Point) {
        var isFirst = true
        do {
            val result = point.fall(cave)
            if (isFirst && result == Outcome.SETTLED) {
                error("BLOCKED")
            }
            isFirst = false
            when(result) {
                Outcome.SETTLED -> break
                Outcome.FELL -> continue
                Outcome.ABYSS -> error("Dropped")
            }
        } while (true)
        cave[point.x, point.y] = SAND
//        printCave(cave)
    }

    fun part1(input: List<String>) {
        var maxX = Int.MIN_VALUE
        var maxY = Int.MIN_VALUE
        for (line in input) {
            val coords = line.split(" -> ")
            for (coord in coords) {
                val (x, y) = coord.split(",").map { it.toInt() }
                maxX = max(x, maxX)
                maxY = max(y, maxY)
            }
        }
        val cave = mk.d2array(maxX + 1 + 10000, maxY + 2) { 0 }
        for (line in input) {
            val coords = line.split(" -> ")
            var prevPoint: Point? = null
            for (coord in coords) {
                val (x, y) = coord.split(",").map { it.toInt() }
                val point = Point(x, y)
                if (prevPoint != null) {
                    rockLine(cave, prevPoint, point)
                }
                prevPoint = point
            }
        }
        printCave(cave)
        var i = 0
        do {
            println(i + 1)
            val start = Point(500, 0)
            simulate(cave, start)
//            break
            i++

//        } while (i < 94)
        } while (true)
    }

    fun part2(input: List<String>) {
    }

    val testInput = readInput("day14/day14")
    part1(testInput)
    part2(testInput)
}
