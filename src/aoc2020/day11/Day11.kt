package aoc2020.day10

import readInput
import java.math.BigInteger

var itemCount = 1

class Item(var worryLevel: BigInteger) {
    val id = itemCount++

    fun bored() {
//        worryLevel /= 3
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Item

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id
    }

    override fun toString(): String {
        return "Item(worryLevel=$worryLevel, id=$id)"
    }
}

class Monkey(val num: Int) {
    var items = mutableListOf<Item>()
    var divBy: BigInteger = BigInteger.ZERO
    lateinit var op: Op
    var trueMonkey = 0
    var falseMonkey = 0
    var inspectCount: BigInteger = BigInteger.ZERO

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Monkey

        if (num != other.num) return false

        return true
    }

    override fun hashCode(): Int {
        return num
    }

    override fun toString(): String {
//        return "Monkey $num: ${items.map { it.worryLevel}.joinToString(", ")}"
        return "Monkey $num"
    }


}

sealed class Op {
    abstract fun apply(item: Item)

    data class Plus(val const: BigInteger): Op() {
        override fun apply(item: Item) {
            item.worryLevel += const
        }
    }

    data class Mul(val const: BigInteger): Op() {
        override fun apply(item: Item) {
            item.worryLevel *= const
        }
    }

    object Square: Op() {
        override fun apply(item: Item) {
            item.worryLevel = item.worryLevel.modPow(2, )
        }

        override fun toString(): String {
            return "Square"
        }
    }
}

fun main() {
    fun printMonkeys(monkeys: List<Monkey>) {
        monkeys.forEach {
            println(it)
        }
    }


    val d2 = 2.toBigInteger()
    val d3 = 3.toBigInteger()
    val d5 = 5.toBigInteger()
    val primes = listOf(2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97).map { it.toBigInteger() }.toSet()
    fun isEven(n: BigInteger) = !n.testBit(0)
    fun isDiv(n: BigInteger, divisor: BigInteger) = n.mod(divisor) == BigInteger.ZERO
    fun isDivBy3(n: BigInteger) = !isEven(n) && isDiv(n, d3)
    fun isDivBy7(n: BigInteger) = !isEven(n) && isDiv(n, d5)

    fun isDivisible(n: BigInteger, divisor: BigInteger): Boolean {
        return when  {
//            divisor == BigInteger.ONE -> true
//            divisor == d2 -> isEven(n)
//            divisor == d3 -> isDivBy3(n)
//            divisor == d5 -> isDivBy5(n)
//            primes.contains(divisor) -> n != divisor && n != BigInteger.ONE
//            d2.isPProbablePrime()
            else -> isDiv(n, divisor)
        }
//        return

    }

    fun round(monkeys: List<Monkey>) {
//        val appends = mutableMapOf<Int, MutableList<Item>>()
        for (monkey in monkeys) {
            for (item in monkey.items) {
                monkey.op.apply(item)
                monkey.inspectCount++
                item.bored()
                val x = item.worryLevel.remainder(monkey.divBy)
                val nextMonkeyNum = if (x == BigInteger.ZERO) {
                    monkey.trueMonkey
                } else {
                    monkey.falseMonkey
                }
                val nextMonkey = monkeys.single { it.num == nextMonkeyNum }
                nextMonkey.items.add(item)
            }
            monkey.items = mutableListOf()
        }

//        for (monkey in monkeys) {
//            val newItems = appends[monkey.num] ?: emptyList()
//            monkey.items = newItems.toMutableList()
//        }
    }

    fun part1(input: List<String>) {
        val monkeys = mutableListOf<Monkey>()
        var monkey: Monkey? = null
        for (line in input) {
            if (line.startsWith("Monkey ")) {
                val (_, numStr) = line.split(" ")
                monkey = Monkey(numStr.dropLast(1).toInt())
                monkeys.add(monkey)
            }
            if (line.startsWith("  Starting items: ")) {
                val items = line.drop("  Starting items: ".length).split(", ").map { it.toInt() }.map {
                    Item(it.toBigInteger())
                }
                monkey!!.items.addAll(items)
            }
            if (line.startsWith("  Operation: ")) {
                val opLine = line.drop("  Operation: ".length)
                val (_, _, left, opStr, right) = opLine.split(" ")
                val op = if (left == "old" && opStr == "*" && right == "old") {
                    Op.Square
                } else if (left == "old" && opStr == "+" && right.all { it.isDigit() }) {
                    Op.Plus(right.toBigInteger())
                } else if (left == "old" && opStr == "*" && right.all {it.isDigit()}) {
                    Op.Mul(right.toBigInteger())
                } else {
                    error("")
                }
                monkey!!.op = op
            }
            if (line.startsWith("  Test: divisible by ")) {
                val opLine = line.drop("  Test: divisible by ".length)
                monkey!!.divBy = opLine.toBigInteger()
            }
            if (line.startsWith("    If true: throw to monkey ")) {
                val opLine = line.drop("    If true: throw to monkey ".length)
                monkey!!.trueMonkey = opLine.toInt()
            }
            if (line.startsWith("    If false: throw to monkey ")) {
                val opLine = line.drop("    If false: throw to monkey ".length)
                monkey!!.falseMonkey = opLine.toInt()
            }
            if (line == "") {
                monkey = null
                continue
            }
        }

//        printMonkeys(monkeys)
//        println("-------")
        repeat(20) {
            println("----- Round ${it + 1}")
            round(monkeys)
//            println("----- After round ${it + 1}")
//            printMonkeys(monkeys)
//            monkeys.forEach {
//                println("$it: Inspect count ${it.inspectCount}")
//            }
        }
//        round(monkeys)
        println("-------")
        monkeys.forEach {
            println("$it: Inspect count ${it.inspectCount}")
        }
        println("-------\nMostActive\n---------------")
        val mostActive = monkeys.sortedByDescending { it.inspectCount }
        printMonkeys(mostActive)
        println(mostActive[0].inspectCount * mostActive[1].inspectCount )

    }

    fun part2(input: List<String>) {
    }

    val testInput = readInput("day11/day11s")
    part1(testInput)
    part2(testInput)
}
