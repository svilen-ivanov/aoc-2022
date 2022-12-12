@file:Suppress("UnstableApiUsage")

package aoc2022.day01

import com.google.common.collect.MinMaxPriorityQueue
import readInput
import java.util.*
import kotlin.Comparator

fun main() {
    fun part1(input: List<String>): Int {
        var max = Int.MIN_VALUE
        var elf = 1
        var i = 1
        var sum = 0
        for (line in input) {
            if (line != "") {
                sum += line.toInt()
            } else {
                if (sum > max) {
                    elf = i
                    max = sum
                } else if (sum == max) {
                    error("sum == max")
                }
                sum = 0
                i++
            }
        }
        println("elf $elf, max $max")
        check(max == 68923)
        return elf
    }

    fun part2(input: List<String>): Int {
        data class ElfCalories(val elf: Int, val cal: Int)

        val topElves: Queue<ElfCalories> = MinMaxPriorityQueue
            .orderedBy(Comparator<ElfCalories> { o1, o2 -> o1.cal.compareTo(o2.cal) }.reversed())
            .maximumSize(3)
            .create()

        var i = 1
        var sum = 0
        for (line in input) {
            if (line != "") {
                sum += line.toInt()
            } else {
                val elfCalories = ElfCalories(i, sum)
                topElves.add(elfCalories)
                sum = 0
                i++
            }
        }

        val sumTop3 = topElves.sumOf(ElfCalories::cal)

        println("sumTop3 $sumTop3")
        println("sumTop1 ${topElves.peek()}")
        check(sumTop3 == 200044)

        return sumTop3
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day01/day01")
    part1(testInput)
    part2(testInput)
}
