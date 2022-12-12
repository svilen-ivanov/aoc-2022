package aoc2022.day11

import readInput
import java.math.BigInteger

var itemCount = 1
val processCount = 10000

class Item(val worryLevelNum: BigInteger) {
    val id = itemCount++

    //    val worryLevel = WorryLevel(id, worryLevelNum)
    val worryLevel: WorryLevel = OptimizedWorryLevel(id, worryLevelNum)
//    val worryLevel: WorryLevel = NaiveWorryLevel(id, worryLevelNum)

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
        return "Monkey $num inspected items $inspectCount times"
    }
}

interface WorryLevel {
    fun plus(v: BigInteger)
    fun mul(v: BigInteger)
    fun square()
    fun isDivisible(div: BigInteger): Boolean
}

class NaiveWorryLevel(
    private val id: Int,
    private val startLevel: BigInteger
) : WorryLevel {
    private var currentLevel = startLevel

    override fun plus(v: BigInteger) {
        currentLevel += v
    }

    override fun mul(v: BigInteger) {
        currentLevel *= v
    }

    override fun square() {
        currentLevel *= currentLevel
    }

    override fun isDivisible(div: BigInteger): Boolean {
        return currentLevel.remainder(div) == BigInteger.ZERO
    }

    override fun toString(): String {
        return "Level for item #$id ($startLevel): $currentLevel"
    }
}

class OptimizedWorryLevel(val id: Int, val startLevel: BigInteger) : WorryLevel {
    val mod = 9699690.toBigInteger()
    private var currentLevel = startLevel

    override fun plus(v: BigInteger) {
        currentLevel += v
        currentLevel = currentLevel.mod(mod)
    }

    override fun mul(v: BigInteger) {
        currentLevel *= v
        currentLevel = currentLevel.mod(mod)

    }

    override fun square() {
        currentLevel *= currentLevel
        currentLevel = currentLevel.mod(mod)

    }

    override fun isDivisible(div: BigInteger): Boolean {
        return currentLevel.remainder(div) == BigInteger.ZERO
    }

    override fun toString(): String {
        return "Level for item #$id ($startLevel): $currentLevel"
    }
}

sealed class Op {
    abstract fun apply(item: Item)

    data class Plus(val const: BigInteger) : Op() {
        override fun apply(item: Item) {
            item.worryLevel.plus(const)
        }
    }

    data class Mul(val const: BigInteger) : Op() {
        override fun apply(item: Item) {
            item.worryLevel.mul(const)
        }
    }

    object Square : Op() {
        override fun apply(item: Item) {
            item.worryLevel.square()
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

    fun round(monkeys: List<Monkey>) {
        val monkeyByNum = monkeys.associateBy { it.num }
        for (monkey in monkeys) {
            for (item in monkey.items) {
                monkey.op.apply(item)
                monkey.inspectCount++
                item.bored()
                val result = item.worryLevel.isDivisible(monkey.divBy)
                val nextMonkeyNum = if (result) {
                    monkey.trueMonkey
                } else {
                    monkey.falseMonkey
                }
                val nextMonkey = monkeyByNum[nextMonkeyNum]!!
                nextMonkey.items.add(item)
            }
            monkey.items = mutableListOf()
        }
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
                } else if (left == "old" && opStr == "*" && right.all { it.isDigit() }) {
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

        val divizors = monkeys.map { it.divBy }.fold(BigInteger.ONE) { acc, bigInteger ->  acc * bigInteger }
        println(divizors)

        repeat(processCount) {
            println("----- Round ${it + 1}")
            round(monkeys)
            println("----- After round ${it + 1}")
            printMonkeys(monkeys)
        }
        println("-----------\nMostActive\n---------------")
        val mostActive = monkeys.sortedByDescending { it.inspectCount }.take(2)
        printMonkeys(mostActive)
        println("Total: ${mostActive[0].inspectCount * mostActive[1].inspectCount}")

    }

    fun part2(input: List<String>) {
    }

    val testInput = readInput("day11/day11")
    part1(testInput)
//    part2(testInput)
}
