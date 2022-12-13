package aoc2022.day13

import readInput
import kotlin.math.min

enum class CompareResult(val comparatorOutcome: Int) {
    CORRECT(-1),
    INCORRECT(1),
    SAME(0),
}

sealed class Packet {
    abstract fun toNumList(): NumList

    data class Num(val num: Int) : Packet() {
        override fun toNumList(): NumList = NumList(mutableListOf(this))

        fun compareWith(right: Num): CompareResult {
            return if (num < right.num) {
                CompareResult.CORRECT
            } else if (num > right.num) {
                CompareResult.INCORRECT
            } else {
                CompareResult.SAME
            }
        }
    }

    data class NumList(
        val items: MutableList<Packet> = mutableListOf()
    ) : Packet(), Comparable<NumList> {
        override fun toNumList() = this

        override fun compareTo(other: NumList): Int {
            return compareWith(other).comparatorOutcome
        }

        fun compareWith(rightSide: NumList): CompareResult {
            val len = min(items.size, rightSide.items.size)
            for (i in 0 until len) {
                val left = items[i]
                val right = rightSide.items[i]
                val result = if (left is Num && right is Num) {
                    left.compareWith(right)
                } else {
                    left.toNumList().compareWith(right.toNumList())
                }
                if (result == CompareResult.SAME) {
                    continue
                } else {
                    return result
                }
            }
            return if (items.size < rightSide.items.size) {
                CompareResult.CORRECT
            } else if (items.size > rightSide.items.size) {
                CompareResult.INCORRECT
            } else {
                CompareResult.SAME
            }
        }
    }
}

val tokens = Regex("(])|(\\[)|(,)|(\\d+)")

fun main() {
    fun parseLine(line: String): Packet.NumList {
        val stack = ArrayDeque<Packet.NumList>()
        val tokens = tokens.findAll(line)
        stack.addLast(Packet.NumList())
        for (tokenMatch in tokens) {
            when (val token = tokenMatch.value) {
                "," -> {}
                "[" -> Packet.NumList().let {
                    stack.last().items.add(it)
                    stack.addLast(it)
                }

                "]" -> stack.removeLast()
                else -> stack.last().items.add(Packet.Num(token.toInt()))
            }
        }
        require(stack.size == 1) {
            "Wrong size; ${stack.size}"
        }
        return stack.last().items.single() as Packet.NumList
    }

    fun part1(input: List<String>) {
        var sum = 0
        for ((i, chunk) in input.chunked(3).withIndex()) {
            val line = i + 1
            val (leftLine, rightLine, _) = chunk
            val leftPacket = parseLine(leftLine)
            val rightPacket = parseLine(rightLine)
            println("Line $line")
            println(leftPacket)
            println(rightPacket)
            val orderResults = leftPacket.compareWith(rightPacket)
            if (orderResults == CompareResult.CORRECT) {
                sum += line
            }
            println(orderResults)
            println("---------------")
        }
        println(sum)
    }

    fun part2(input: List<String>) {
        val packets = mutableListOf<Packet.NumList>()
        val div1 = Packet.NumList(mutableListOf(Packet.NumList(mutableListOf(Packet.Num(2)))))
        packets.add(div1)
        val div2 = Packet.NumList(mutableListOf(Packet.NumList(mutableListOf(Packet.Num(6)))))
        packets.add(div2)
        for (chunk in input.chunked(3)) {
            val (leftLine, rightLine, _) = chunk
            val leftPacket = parseLine(leftLine)
            val rightPacket = parseLine(rightLine)
            packets.add(leftPacket)
            packets.add(rightPacket)
        }
        packets.sort()
        println((packets.indexOf(div1) + 1) * (packets.indexOf(div2) + 1))
    }

    val testInput = readInput("day13/day13")
    part1(testInput)
    part2(testInput)
}
