package aoc2020.day07

import readInput

sealed class Fs() {
    abstract val parent: Dir?
    abstract val name: String
    abstract fun size(): Int

    data class File(override val parent: Dir?, override val name: String, val size: Int) : Fs() {
        override fun size(): Int = size
    }

    data class Dir(override val parent: Dir?, override val name: String) : Fs() {
        val entries: MutableList<Fs> = mutableListOf()
        override fun size() = entries.sumOf { it.size() }
    }
}

fun flatten(dir: Fs.Dir): List<Fs.Dir> {
    val list = mutableListOf<Fs.Dir>()
    flatten(dir, list)
    return list
}

fun flatten(dir: Fs.Dir, list: MutableList<Fs.Dir>) {
    val dirs = dir.entries.filterIsInstance<Fs.Dir>()
    list.addAll(dirs)
    dirs.forEach { flatten(it, list) }
}

fun parseInput(input: List<String>): Fs.Dir {
    val root = Fs.Dir(null, "/")
    var current = root
    for ((index, line) in input.withIndex()) {
        if (index == 0) continue
        if (line.startsWith("$")) {
            val commandList = line.split(" ")
            val commandName = commandList[1]
            when (commandName) {
                "cd" -> {
                    val arg = commandList[2]
                    if (arg == "..") {
                        current = current.parent!!
                    } else {
                        var dir = current.entries.find { it.name == arg }
                        if (dir == null) {
                            dir = Fs.Dir(current, arg)
                            current.entries.add(dir)
                        }
                        current = dir as Fs.Dir
                    }
                }
                "ls" -> {

                }
            }

        } else {
            val entry = line.split(" ")
            if (entry[0] == "dir") {
                current.entries.add(Fs.Dir(current, entry[1]))
            } else {
                current.entries.add(Fs.File(current, entry[1], entry[0].toInt()))
            }
        }
    }
    return root
}


fun main() {
    fun part1(input: List<String>) {
        val root = parseInput(input)
        val size = flatten(root).filter { it.size() <= 100000 }.sumOf { it.size() }
        println(size)
        check(size == 1749646)
    }

    fun part2(input: List<String>) {
        val root = parseInput(input)
        val totalSize = 70000000
        val sizeGoal = 30000000
        val currentSize = root.size()
        val currentUnused = totalSize - currentSize
        val targetSizeToDelete = sizeGoal - currentUnused
        println("$currentSize, $currentUnused, $targetSizeToDelete")

        val dirs = flatten(root).filter {
            targetSizeToDelete < it.size()
        }.minBy { it.size() }
        println(dirs.name)
        println(dirs.size())
        check(dirs.size() == 1498966)

    }

    val testInput = readInput("day07/day07")
    part1(testInput)
    part2(testInput)
}
