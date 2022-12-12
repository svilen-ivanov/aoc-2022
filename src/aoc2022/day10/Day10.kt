package aoc2022.day10

import readInput
import kotlin.math.abs

fun main() {
    fun part1(input: List<String>) {
        var x = 1
        val valByCycle = mutableListOf(x)
        for (line in input) {
            val commandLine = line.split(" ")
            val commandName = commandLine[0]
            val arg = if (commandLine.size > 1) commandLine.last().toInt() else null
            when (commandName) {
                "noop" -> {
                    valByCycle.add(x)
                }

                "addx" -> {
                    requireNotNull(arg)
                    valByCycle.add(x)
                    valByCycle.add(x)
                    x += arg
                }
            }
        }
        val res = (0..5).map { 40 * it + 20 }.sumOf {
            it * valByCycle[it]
        }
        println(res)
    }

    fun printScreen(screen: MutableList<String>) {
        screen.forEachIndexed { index, s ->
            print(s)
            if ((index + 1) % 40 == 0) {
                println()
            }
        }
    }

    fun part2(input: List<String>) {
        var x = 1
        val valByCycle = mutableListOf<Int>()
        for (line in input) {
            val commandLine = line.split(" ")
            val commandName = commandLine[0]
            val arg = if (commandLine.size > 1) commandLine.last().toInt() else null
            when (commandName) {
                "noop" -> {
                    valByCycle.add(x)
                }

                "addx" -> {
                    requireNotNull(arg)
                    valByCycle.add(x)
                    valByCycle.add(x)
                    x += arg
                }
            }
        }

        val screen = mutableListOf<String>()
        val size = 40 * 6
        screen.addAll((1..size).map { "." })

        for ((cycle, xValue) in valByCycle.withIndex()) {
            val screenPos = cycle % size
            val xPos = cycle % 40
            if (abs(xPos - xValue) <= 1) {
                screen[screenPos] = "*"
            } else {
                screen[screenPos] = "."
            }
            println("Cycle: $cycle, X=$xValue")
            println("-------------------------")
            printScreen(screen)
            println("-------------------------")
        }

        printScreen(screen)
    }

    val testInput = readInput("day10/day10")
    part1(testInput)
    part2(testInput)
}
