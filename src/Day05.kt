fun parseConfig(input: List<String>): List<ArrayDeque<String>> {
    val stacks = mutableListOf<ArrayDeque<String>>()

    for ((index, line) in input.withIndex()) {
        val chunks = line.chunked(4)
        val stackEntries = chunks.map { it[1].toString().trim() }
        if (index == 0) {
            repeat(stackEntries.size) {
                stacks.add(ArrayDeque())
            }
        }
        if (index != input.size - 1) {
            stackEntries.forEachIndexed { stackIndex, stackEntry ->
                if (stackEntry.isNotBlank()) {
                    stacks[stackIndex].add(stackEntry)
                }
            }
        }

    }
    println(stacks)
    return stacks
}

fun main() {
    fun part1(input: List<String>) {
        val split = input.indexOf("")
        val config = input.subList(0, split)
        val stacks = parseConfig(config)
        val commands = input.subList(split + 1, input.size)
        commands.forEach { commandLine ->
            val command = commandLine.split(" ")
            val count = command[1].toInt()
            val sourceIndex = command[3].toInt() - 1
            val destIndex = command[5].toInt() - 1
            val sourceStack = stacks[sourceIndex]
            val destStack = stacks[destIndex]
            repeat(count) {
                destStack.addFirst(sourceStack.removeFirst())
            }
        }

        val result = stacks.joinToString("") { it.first() }
        println(result)
        check(result == "BWNCQRMDB")
    }

    fun part2(input: List<String>) {
        val split = input.indexOf("")
        val config = input.subList(0, split)
        val stacks = parseConfig(config)
        val commands = input.subList(split + 1, input.size)
        commands.forEach { commandLine ->
            val command = commandLine.split(" ")
            val count = command[1].toInt()
            val sourceIndex = command[3].toInt() - 1
            val destIndex = command[5].toInt() - 1
            val sourceStack = stacks[sourceIndex]
            val destStack = stacks[destIndex]

            val pack = (1..count).map { sourceStack.removeFirst() }
            pack.reversed().forEach { destStack.addFirst(it) }
        }

        val result = stacks.joinToString("") { it.first() }
        println(result)
        check(result == "NHWZCBNBF")
    }

    val testInput = readInput("day05")
    part1(testInput)
    part2(testInput)
}
