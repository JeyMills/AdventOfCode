package me.jmoulins.year_2021

import java.io.File
import java.lang.Character

fun main() {
    var gridTest = readDay11File("src/main/resources/year_2021/day11/input-test.txt")
    var grid = readDay11File("src/main/resources/year_2021/day11/input.txt")

    println("PART 1")
    println("There have been ${part1(gridTest)} flashes after 100 steps for input test")
    println("There have been ${part1(grid)} flashes after 100 steps for input")

    gridTest = readDay11File("src/main/resources/year_2021/day11/input-test.txt")
    grid = readDay11File("src/main/resources/year_2021/day11/input.txt")
    println("PART 2")
    println("All octopuses flash at step ${part2(gridTest)} for input test")
    println("All octopuses flash at step ${part2(grid)} for input")
}

fun readDay11File(path: String): Grid {
    return File(path)
        .readLines()
        .let(Grid::parse)
}

fun part1(grid: Grid): Int {
    for (i in 1..100) {
        grid.runStep()
    }
    return grid.octopuses.sumOf { list: List<Octopus> -> list.sumOf { octopus: Octopus -> octopus.flashCounter } }
}

fun part2(grid: Grid): Int {
    grid.printGrid()
    var step = 0
    do {
        step++
        var allOctopusFlash = grid.runStep()
    } while (!allOctopusFlash)
    return step
}

data class Octopus(
    var energy: Int,
) {
    var alreadyFlash: Boolean = false
    var flashCounter: Int = 0

    fun incEnergy() {
        if (energy <= 9) {
            energy++
        }
    }

    fun hasEnoughEnergyToFlash(): Boolean {
        return energy > 9
    }
}

data class Grid(
    val octopuses: List<List<Octopus>>,
) {

    fun runStep(): Boolean {
        // Increase all by 1
        for (line in octopuses) {
            for (octopus in line) {
                octopus.incEnergy()
            }
        }

        // Flash
        do {
            var haveNewOctopusReadyToFlash = false
            for (i in octopuses.indices) {
                for (j in octopuses[i].indices) {
                    var octopus = octopuses[i][j]
                    if (octopus.hasEnoughEnergyToFlash() && !octopus.alreadyFlash) {
                        haveNewOctopusReadyToFlash = true
                        if (i != 0 && j != 0) {
                            octopuses[i - 1][j - 1].incEnergy()
                        }
                        if (i != 0) {
                            octopuses[i - 1][j].incEnergy()
                        }
                        if (i != 0 && j != octopuses.size - 1) {
                            octopuses[i - 1][j + 1].incEnergy()
                        }
                        if (j != 0) {
                            octopuses[i][j - 1].incEnergy()
                        }
                        if (j != octopuses.size - 1) {
                            octopuses[i][j + 1].incEnergy()
                        }
                        if (i != octopuses[i].size - 1 && j != 0) {
                            octopuses[i + 1][j - 1].incEnergy()
                        }
                        if (i != octopuses[i].size - 1) {
                            octopuses[i + 1][j].incEnergy()
                        }
                        if (i != octopuses[i].size - 1 && j != octopuses.size - 1) {
                            octopuses[i + 1][j + 1].incEnergy()
                        }
                        octopus.alreadyFlash = true
                        octopus.flashCounter += 1
                    }
                }
            }
        } while (haveNewOctopusReadyToFlash)

        // Reset alreadyFlash boolean to false for next step
        octopuses.forEach {
            it
                .filter { octopus -> octopus.alreadyFlash }
                .forEach { octopus ->
                    run {
                        octopus.energy = 0
                        octopus.alreadyFlash = false
                    }
                }
        }

        // Return true if all octopus have energy to 0
        return octopuses.sumOf { it.count { octopus -> octopus.energy == 0 } } == 100
    }

    fun printGrid() {
        for (i in octopuses) {
            for (j in i) {
                print("${j.energy}\t")
            }
            println()
        }
        println()
    }

    companion object {
        fun parse(strings: List<String>) = Grid(
            octopuses = strings.map { s: String -> s.map { Octopus(Character.getNumericValue(it)) } }
        )
    }
}