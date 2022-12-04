fun String.toRange(): IntRange {
    val startEnd = split('-').map { it.toInt() }
    return startEnd[0]..startEnd[1]
}

fun IntRange.contains(other: IntRange): Boolean {
    return contains(other.first) && contains(other.last)
}

fun IntRange.overlap(other: IntRange): Boolean {
    return other.contains(this) || contains(other.first) || contains(other.last)
}

fun main() {
    fun part1(input: List<String>) {
        var sum = 0
        for (line in input) {
            val (pair1, pair2) = line.split(',')
            val range1 = pair1.toRange()
            val range2 = pair2.toRange()
            if (range1.contains(range2) || range2.contains(range1)) {
                sum++
            }

        }
        println(sum)
        check(sum == 560)
    }

    fun part2(input: List<String>) {
        var sum = 0

        for (line in input) {
            val (pair1, pair2) = line.split(',')
            val range1 = pair1.toRange()
            val range2 = pair2.toRange()
            if (range1.overlap(range2)) {
                sum++
            }
        }
        println(sum)
        check(sum == 839)
    }

    val testInput = readInput("day04")
    part1(testInput)
    part2(testInput)
}
