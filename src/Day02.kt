import Outcome.*

enum class RockPaperScissors(
    val otherSymbol: String,
    val mySymbol: String,
    val myScore: Int,
    val order: Int,
) {
    ROCK("A", "X", 1, 1),
    SCISSORS("C", "Z", 3, 2),
    PAPER("B", "Y", 2, 3),
}

enum class Outcome(val symbol: String) {
    WIN("Z"),
    TIE("Y"),
    LOSE("X")
}

object Game {
    private val all = RockPaperScissors.values().toList().sortedBy(RockPaperScissors::order)

    private fun RockPaperScissors.getOutcome(other: RockPaperScissors): Outcome {
        if (this == other) {
            return TIE
        }
        val winsOver = getWinsOver()
        return if (other == winsOver) {
            WIN
        } else {
            LOSE
        }
    }

    private fun RockPaperScissors.getWinsOver(): RockPaperScissors {
        val index = all.indexOf(this)
        val isLast = index == all.size - 1
        val winsOver = if (isLast) {
            all.first()
        } else {
            all[index + 1]
        }
        return winsOver
    }

    private fun RockPaperScissors.getLoosesFrom(): RockPaperScissors {
        val index = all.indexOf(this)
        val isFirst = index == 0
        val losesFrom = if (isFirst) {
            all.last()
        } else {
            all[index - 1]
        }
        return losesFrom
    }

    fun scoreGame(me: RockPaperScissors, other: RockPaperScissors) = when (me.getOutcome(other)) {
        WIN -> 6
        TIE -> 3
        LOSE -> 0
    }

    fun getMyMoveOnOutcome(expectedOutcome: Outcome, otherMove: RockPaperScissors) =
        when (expectedOutcome) {
            WIN -> otherMove.getLoosesFrom()
            TIE -> otherMove
            LOSE -> otherMove.getWinsOver()
        }

}

val convertFromOtherSymbol = RockPaperScissors.values().associateBy { it.otherSymbol }
val convertFromMySymbol = RockPaperScissors.values().associateBy { it.mySymbol }
val convertOutcome = Outcome.values().associateBy { it.symbol }

fun main() {
    fun part1(input: List<String>) {
        var sum = 0
        for (line in input) {
            val (otherSymbol, mySymbol) = line.split(" ")
            val otherMove = convertFromOtherSymbol[otherSymbol]!!
            val myMove = convertFromMySymbol[mySymbol]!!
            val score = Game.scoreGame(myMove, otherMove) + myMove.myScore
            sum += score
        }
        println("$sum")
        check(sum == 15632)
    }

    fun part2(input: List<String>) {
        var sum = 0
        for (line in input) {
            val (otherSymbol, expectedOutcomeSymbol) = line.split(" ")
            val otherMove = convertFromOtherSymbol[otherSymbol]!!
            val expectedOutcome = convertOutcome[expectedOutcomeSymbol]!!
            val myMove = Game.getMyMoveOnOutcome(expectedOutcome, otherMove)
            val score = Game.scoreGame(myMove, otherMove) + myMove.myScore
            sum += score
        }
        println("$sum")
        check(sum == 14416)
    }

    val testInput = readInput("day02")
    part1(testInput)
    part2(testInput)
}
