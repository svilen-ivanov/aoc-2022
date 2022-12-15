package aoc2022.day15

import aoc2022.day04.overlap
import com.google.common.collect.Comparators
import readInput
import java.util.*
import kotlin.math.absoluteValue
import kotlin.math.max
import kotlin.math.min
import kotlin.reflect.KClass

val number = "((?:-)?\\d+)"
val regex = Regex("Sensor at x=$number, y=$number: closest beacon is at x=$number, y=$number")

sealed class Point {
    abstract val x: Int
    abstract val y: Int

    fun mdist(other: Point): Int {
        return (x - other.x).absoluteValue + (y - other.y).absoluteValue
    }

    data class Sensor(override val x: Int, override val y: Int) : Point()
    data class Beacon(override val x: Int, override val y: Int) : Point()
    data class Coverage(override val x: Int, override val y: Int) : Point()
    data class PossibleSource(override val x: Int, override val y: Int) : Point()
}

data class RangeWithPoint(
    val range: IntRange,
    val point: KClass<out Point>,
)

val comparatorFirst: Comparator<RangeWithPoint> = Comparator.comparing { it.range.first }
val comparatorLast: Comparator<RangeWithPoint> = Comparator.comparing { it.range.last }

class Day15(val input: List<String>) {
    //    var yLineIntestections = TreeMap<Int, KClass<out Point>>()
    var yLine = 0

    val ranges = mutableSetOf<RangeWithPoint>()

    fun updateYLine(from: Int, to: Int, type: KClass<out Point>) {
        check(from <= to)
        if (to > 4000000 && from > 4000000) return
        val f = max(0, from)
        val t = min(to, 4000000)
//        val f = from
//        val t = to
        ranges.add(RangeWithPoint(f..t, type))
//        ranges.add(from..to)
////        val f = if (from < 0) 0 else from
////        val t = if (from >= 4000000) 4000000 else to
//        val f = from
//        val t = to
//        for (i in f..t) {
//            val pos = yLineIntestections[i]
//            if (pos == null) {
//                yLineIntestections.put(i, type)
//            } else if (pos == Point::Beacon::class || pos == Point::Sensor::class) {
//                continue
//            }
//        }
    }

    fun findIntesection(from: Point.Coverage, to: Point.Coverage): Int {
//        println("Intersection from: ${from} to ${to}, yline=${yLine}")
        val xDir = if (from.x < to.x) {
            1
        } else if (from.x > to.x) {
            -1
        } else {
            0
        }
        return from.x + (xDir * (yLine - from.y).absoluteValue)
    }

    fun updateCoverage(sensor: Point.Sensor, beacon: Point.Beacon) {
        val distance = sensor.mdist(beacon)
        if (sensor.y == yLine) {
            updateYLine(sensor.x - distance, sensor.x - 1, Point.Coverage::class)
            updateYLine(sensor.x, sensor.x, Point.Sensor::class)
            updateYLine(sensor.x + 1, sensor.x + distance, Point.Coverage::class)
            return
        }
        if (beacon.y == yLine) {
            updateYLine(beacon.x, beacon.x, Point.Beacon::class)
        }
        val north = Point.Coverage(sensor.x, sensor.y - distance)
        val south = Point.Coverage(sensor.x, sensor.y + distance)
        if (north.y > yLine || south.y < yLine) {
            return
        }
        if (north.y == yLine) {
            updateYLine(north.x, north.x, Point.Coverage::class)
            return
        }
        if (south.y == yLine) {
            updateYLine(south.x, south.x, Point.Coverage::class)
            return
        }

        val east = Point.Coverage(sensor.x + distance, sensor.y)
        val west = Point.Coverage(sensor.x - distance, sensor.y)

        if (sensor.y > yLine) {
//            println("find where n->w, n->e intersect the y line")
            updateYLine(findIntesection(north, west), findIntesection(north, east), Point.Coverage::class)
        } else {
//            println("find where w->s, e->s intersect the y line")
            updateYLine(findIntesection(west, south), findIntesection(east, south), Point.Coverage::class)
        }
    }

    fun findCoverage() {
        val possibleSources = mutableListOf<Point.PossibleSource>()

        for (l in 0..4000000) {
            yLine = l
            ranges.clear()
            for ((index, line) in input.withIndex()) {
                val (x1, y1, x2, y2) = regex.find(line)!!.groupValues.drop(1).map { it.toInt() }
                val sensor = Point.Sensor(x1, y1)
                val beacon = Point.Beacon(x2, y2)
                updateCoverage(sensor, beacon)
            }
            val before = ranges.map { it.range }
            val mergedRanges = merge(before)
            val count = mergedRanges.sumOf { it.size() }
            println("-> ${yLine}: ${count}")
            if (count <= 4000000) {
                println(mergedRanges)
                error("")
            }
        }
//        println(possibleSources)

    }

    private fun merge(ranges: List<IntRange>): List<IntRange> {
//        println("Merging: ${ranges.joinToString(", ")}")
        val result = mutableListOf<IntRange>()
        var merged = false
        outer@ for (i in ranges.indices) {
            for (j in i + 1 until ranges.size) {
//                print("checking $i, $j => ${ranges[i]} and ${ranges[j]}...")
                if (ranges[i].canMerge(ranges[j])) {
                    val newRange = ranges[i].merge(ranges[j])
//                    println("can merge -> ${newRange}")
                    result.add(newRange)
                    result.addAll(ranges.filterIndexed { index, _ -> index != i && index != j })
                    merged = true
                    break@outer
                } else {
//                    println("can't merge :(")
                }
            }
        }
        if (merged) {
            return merge(result)
        } else {
            return ranges
        }
    }

}

fun main() {


    fun part1(input: List<String>) {
        val day15 = Day15(input)
        day15.findCoverage()
    }

    fun part2(input: List<String>) {
    }

    val testInput = readInput("day15/day15")
    part1(testInput)
    part2(testInput)
}

fun IntRange.size() = last - first + 1

//
fun IntRange.canMerge(other: IntRange): Boolean {
    return !(last < other.first || other.last < first)
}

fun IntRange.merge(other: IntRange): IntRange {
    if (first <= other.first && other.last <= last) {
        return this
    }
    if (other.first <= first && last <= other.last) {
        return other
    }

    val all = listOf(other.first, other.last, first, last)
    return all.min()..all.max()
}

object Test {
    @JvmStatic
    fun main(args: Array<String>) {
        val a = 16..24
        val b = 14..18
        println(a.canMerge(b))
        println(a.merge(b))
    }
}
