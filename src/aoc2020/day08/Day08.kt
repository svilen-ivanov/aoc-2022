package aoc2020.day08

import readInput

@OptIn(ExperimentalStdlibApi::class)
fun main() {
    fun part1(input: List<String>) {
        val map: MutableList<List<Int>> = mutableListOf()
        for (line in input) {
            map.add(line.map { it.toString().toInt() })
        }
        val rows = map.size
        val cols = map.first().size

        val visible = mutableSetOf<Pair<Int, Int>>()

        for (x in 0.rangeUntil(rows)) {
            var max = Int.MIN_VALUE
            for (y in 0.rangeUntil(cols)) {
                val tree = map[x][y]
                if (tree > max) {
                    max = tree
                    visible.add(x to y)
                }
            }
        }

        for (x in 0.rangeUntil(rows)) {
            var max = Int.MIN_VALUE
            for (y in 0.rangeUntil(cols).reversed()) {
                val tree = map[x][y]
                if (tree > max) {
                    max = tree
                    visible.add(x to y)
                }
            }
        }

        for (y in 0.rangeUntil(cols)) {
            var max = Int.MIN_VALUE
            for (x in 0.rangeUntil(rows)) {
                val tree = map[x][y]
                if (tree > max) {
                    max = tree
                    visible.add(x to y)
                }
            }
        }

        for (y in 0.rangeUntil(cols)) {
            var max = Int.MIN_VALUE
            for (x in 0.rangeUntil(rows).reversed()) {
                val tree = map[x][y]
                if (tree > max) {
                    max = tree
                    visible.add(x to y)
                }
            }
        }

        println(visible.size)
        check(visible.size == 1820)
    }

    fun calcScore(map: MutableList<List<Int>>, x: Int, y: Int): Int {
        val rows = map.size
        val cols = map.first().size

        // left
        val tree = map[y][x]

        var count1 = 0
        for (cx in (0 until x).reversed()) {
            val h = map[y][cx]
            if (h < tree) {
                count1++
            }
            if (h >= tree) {
                count1++
                break
            }
        }

        // right
        var count2 = 0
        for (cx in (x + 1 until cols)) {
            val h = map[y][cx]
            if (h < tree) {
                count2++
            }
            if (h >= tree) {
                count2++
                break
            }
        }

        // down
        var count3 = 0
        for (cy in (0 until y).reversed()) {
            val h = map[cy][x]
            if (h < tree) {
                count3++
            }
            if (h >= tree) {
                count3++
                break
            }
        }

        // down
        var count4 = 0
        for (cy in (y + 1 until rows)) {
            val h = map[cy][x]
            if (h < tree) {
                count4++
            }
            if (h >= tree) {
                count4++
                break
            }
        }
        return count1 * count2 * count3 * count4
    }

    fun part2(input: List<String>) {
        val map: MutableList<List<Int>> = mutableListOf()
        for (line in input) {
            map.add(line.map { it.toString().toInt() })
        }
        val rows = map.size
        val cols = map.first().size
        var score = Int.MIN_VALUE
        for (y in (0 + 1).rangeUntil(rows - 1)) {
            for (x in (0 + 1).rangeUntil(cols - 1)) {
                val currentScore = calcScore(map, x, y)
                score = maxOf(score, currentScore)
            }
        }

        println(score)
        check(score == 385112)

    }

    val testInput = readInput("day08/day08")
    part1(testInput)
    part2(testInput)
}
