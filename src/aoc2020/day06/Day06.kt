package aoc2020.day06

import readInput

fun main() {
    fun findMarker(line: String, uniqueCount: Int): Int {
        val deque = ArrayDeque<Char>()
        for ((index, char) in line.withIndex()) {
            deque.addLast(char)
            if (deque.size < uniqueCount) continue
            if (deque.toSet().size == uniqueCount) {
                val pos = index + 1
                println(pos)
                return pos
            } else {
                deque.removeFirst()
            }
        }
        error("")
    }

    fun part1(input: List<String>) {
        for (line in input) {
            check(findMarker(line, 4) == 1779)
        }
    }

    fun part2(input: List<String>) {
        for (line in input) {
            check(findMarker(line, 14) == 2635)
        }
    }

    val testInput = readInput("day06/day06")
    part1(testInput)
    part2(testInput)
}
