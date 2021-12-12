package me.jmoulins.year_2021

import java.io.File

fun main() {
    val inputTest = buildCaveSystem("src/main/resources/year_2021/day12/input-test.txt")
    val inputTest2 = buildCaveSystem("src/main/resources/year_2021/day12/input-test-2.txt")
    val inputTest3 = buildCaveSystem("src/main/resources/year_2021/day12/input-test-3.txt")
    val input = buildCaveSystem("src/main/resources/year_2021/day12/input.txt")

    println("PART 1")
    println("There are ${part1(inputTest)} paths possible for input-test.txt")
    println("There are ${part1(inputTest2)} paths possible for input-test-2.txt")
    println("There are ${part1(inputTest3)} paths possible for input-test-3.txt")
    println("There are ${part1(input)} paths possible for input.txt")
    println()
    println("PART 2")
    println("There are ${part2(inputTest)} paths possible for input-test.txt")
    println("There are ${part2(inputTest2)} paths possible for input-test-2.txt")
    println("There are ${part2(inputTest3)} paths possible for input-test-3.txt")
    println("There are ${part2(input)} paths possible for input.txt")
}

fun buildCaveSystem(path: String): List<Cave> {
    val caveSystem: MutableList<Cave> = mutableListOf()
    val lines = File(path).readLines()

    lines.forEach { s: String ->
        run {
            val connection = s.split("-")
            var caveA = Cave(connection[0])
            var caveB = Cave(connection[1])
            if (caveSystem.contains(caveA)) {
                caveA = caveSystem.first { cave -> cave.name == caveA.name }
            } else {
                caveSystem.add(caveA)
            }
            if (caveSystem.contains(caveB)) {
                caveB = caveSystem.first { cave -> cave.name == caveB.name }
            } else {
                caveSystem.add(caveB)
            }
            caveA.addConnection(caveB)
        }
    }
    return caveSystem
}

fun part1(caveSystem: List<Cave>): Int {
    val startCave = findStartCave(caveSystem)
    val endCave = findEndCave(caveSystem)
    val paths: MutableList<MutableList<Cave>> = mutableListOf()
    val currentPath: MutableList<Cave> = mutableListOf()
    explorePart1(endCave, startCave, paths, currentPath)
    return paths.size
}

fun explorePart1(
    endCave: Cave,
    currentCave: Cave,
    paths: MutableList<MutableList<Cave>>,
    currentPath: MutableList<Cave>
) {
    currentPath.add(currentCave)
    if (currentCave == endCave) {
        paths.add(currentPath)
        return
    }
    for (cave in currentCave.connections) {
        if ((cave.isSmallCave() && currentPath.contains(cave))) continue
        explorePart1(endCave, cave, paths, currentPath.toMutableList())
    }
}

fun part2(caveSystem: List<Cave>): Int {
    val startCave = findStartCave(caveSystem)
    val endCave = findEndCave(caveSystem)
    val paths: MutableList<MutableList<Cave>> = mutableListOf()
    val currentPath: MutableList<Cave> = mutableListOf()
    explorePart2(endCave, startCave, paths, currentPath)
    return paths.size
}

fun explorePart2(
    endCave: Cave,
    currentCave: Cave,
    paths: MutableList<MutableList<Cave>>,
    currentPath: MutableList<Cave>
) {
    currentPath.add(currentCave)
    if (currentCave == endCave) {
        paths.add(currentPath)
        return
    }
    for (cave in currentCave.connections) {
        if (cave.name == "start" || (cave.isSmallCave() && currentPath.contains(cave) && currentPath.filter { it.isSmallCave() }
                .groupingBy { it.name }.eachCount().filter { (it.value == 2) }.isNotEmpty())) continue
        explorePart2(endCave, cave, paths, currentPath.toMutableList())
    }
}

fun findStartCave(caveSystem: List<Cave>): Cave {
    return caveSystem.first { cave -> cave.name == "start" }
}

fun findEndCave(caveSystem: List<Cave>): Cave {
    return caveSystem.first { cave -> cave.name == "end" }
}

data class Cave(
    val name: String,
) {
    val connections: MutableList<Cave> = mutableListOf()

    fun isSmallCave(): Boolean {
        return name.toCharArray().all { it.isLowerCase() }
    }

    fun addConnection(cave: Cave) {
        connections.add(cave)
        cave.connections.add(this)
    }

    override fun equals(other: Any?): Boolean = (other is Cave && other.name == this.name)
}