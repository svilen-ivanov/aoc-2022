package aoc2020.day09

import readInput
import kotlin.math.absoluteValue

interface Knot {
    val x: Int;
    val y: Int;
}

class Head(override var x: Int, override var y: Int) : Knot

class Tail(val head: Knot) : Knot {
    fun update() {
        val distX = (head.x - x).absoluteValue
        val distY = (head.y - y).absoluteValue
        if (distX <= 1 && distY <= 1) {
            return
        } else {
            if (distX > 1) {
                if (x < head.x) {
                    x++
                } else {
                    x--
                }
                if (distY == 1) {
                    y = head.y
                }
            }
            if (distY > 1) {
                if (y < head.y) {
                    y++
                } else {
                    y--
                }
                if (distX == 1) {
                    x = head.x
                }
            }
        }
        check((head.x - x).absoluteValue <= 1 && (head.y - y).absoluteValue <= 1)
    }

    override var x: Int = head.x
    override var y: Int = head.y
}

class Rope(
    val head: Head,
    val tail: Tail
) {
    val tailVisited = mutableSetOf<Pair<Int, Int>>()

    fun moveHead(dir: String, times: Int) {
        repeat(times) {
            when (dir) {
                "U" -> {
                    head.y++
                    tail.update()
                }

                "D" -> {
                    head.y--
                    tail.update()
                }

                "L" -> {
                    head.x--
                    tail.update()
                }

                "R" -> {
                    head.x++
                    tail.update()
                }
            }
            println("$dir\n------------------------")
            print()
            tailVisited.add(Pair(tail.x, tail.y))
        }
    }

    fun result() {
        println(tailVisited.size)
        check(tailVisited.size == 5907)

    }

    fun print() {
        (0..5).reversed().forEach { y ->
            (0..5).forEach { x ->
                if (x == head.x && y == head.y) {
                    print('H')
                } else if (x == tail.x && y == tail.y) {
                    print('T')
                } else {
                    print('.')
                }
            }
            println()
        }
    }

}

class Rope2(
    val head: Head,
    val tails: List<Tail>
) {
    val tailVisited = mutableSetOf<Pair<Int, Int>>()
    val lastTail = tails.last()

    companion object {
        fun create(): Rope2 {
            val head = Head(0, 0)
            var prev: Knot = head
            val tails = (1..9).map {
                Tail(prev).also { prev = it }
            }
            return Rope2(head, tails)
        }
    }

    fun moveHead(dir: String, times: Int) {
        repeat(times) {
            when (dir) {
                "U" -> {
                    head.y++
                    updateTails()
                }

                "D" -> {
                    head.y--
                    updateTails()
                }

                "L" -> {
                    head.x--
                    updateTails()
                }

                "R" -> {
                    head.x++
                    updateTails()
                }
            }
            tailVisited.add(Pair(lastTail.x, lastTail.y))
        }
        println("$dir $times\n------------------------")
        print()

    }

    private fun updateTails() {
        tails.forEach { it.update() }
    }

    fun result() {
        println(tailVisited.size)
        check(tailVisited.size == 2303)
    }

    fun print() {
        val (xMin, xMax) = (listOf(head) + tails).sortedBy { it.x }.run {
            Pair(first().x, last().x)
        }
        val (yMin, yMax) = (listOf(head) + tails).sortedBy { it.y }.run {
            Pair(first().y, last().y)
        }
        (yMin - 10..yMax + 10).reversed().forEach { y ->
            (xMin - 10..xMax + 10).forEach { x ->
                if (x == head.x && y == head.y) {
                    print('H')
                } else {
                    var printed = false
                    for ((index, tail) in tails.withIndex()) {
                        if (tail.x == x && tail.y == y) {
                            print(index + 1)
                            printed = true
                            break
                        }
                    }
                    if (!printed) {
                        print('.')
                    }
                }
            }
            println()
        }
    }

}

fun main() {
    fun part1(input: List<String>) {
        val head = Head(0, 0)
        val tail = Tail(head)
        val rope = Rope(head, tail)

        for (line in input) {
            val (dir, countStr) = line.split(" ")
            val count = countStr.toInt()
            rope.moveHead(dir, count)
        }
        rope.result()
    }

    fun part2(input: List<String>) {
        val rope = Rope2.create()
        for (line in input) {
            val (dir, countStr) = line.split(" ")
            val count = countStr.toInt()
            rope.moveHead(dir, count)
        }
        rope.result()
    }

    val testInput = readInput("day09/day09")
    part1(testInput)
    part2(testInput)
}
