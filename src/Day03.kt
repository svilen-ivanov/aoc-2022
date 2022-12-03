fun priority(char: Char): Int {
    return when {
        ('a'..'z').contains(char) -> 1 + char.code - 'a'.code
        ('A'..'Z').contains(char) -> 27 + char.code - 'A'.code
        else -> error("")
    }
}

fun main() {
    fun part1(input: List<String>) {
        var sum = 0
        for (line in input) {
            val item = line
                .chunked(line.length / 2)
                .map(String::toSortedSet)
                .reduce { acc, items -> acc.apply { retainAll(items) } }
                .single()
            sum += priority(item)
        }
        println(sum)
        check(sum == 7967)
    }

    fun part2(input: List<String>) {
        var sum = 0

        for (group in input.chunked(3)) {
            val item = group
                .map(String::toSortedSet)
                .reduce { acc, items -> acc.apply { retainAll(items) } }
                .single()

            sum += priority(item)
        }
        println(sum)
        check(sum == 2716)
    }

    fun part2fp(input: List<String>) {
        val sum = input
            .chunked(3)
            .sumOf { group ->
                group.map(String::toSortedSet)
                    .reduce { acc, items -> acc.apply { retainAll(items) } }
                    .map { priority(it) }
                    .single()
            }
        println(sum)
        check(sum == 2716)
    }

    val testInput = readInput("day03")
    part1(testInput)
    part2(testInput)
    part2fp(testInput)
}
