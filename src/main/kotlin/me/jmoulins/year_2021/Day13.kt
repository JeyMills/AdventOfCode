package me.jmoulins.year_2021

import java.io.File

fun main() {
    val inputTest = buildMatrix("src/main/resources/year_2021/day13/input-test.txt")
    val input = buildMatrix("src/main/resources/year_2021/day13/input.txt")

    println("PART 1")
    println("There are ${part1(inputTest)} dots for input-test.txt")
    println("There are ${part1(input)} dots for input.txt")
    println()

    println("PART 2")
    part2(inputTest)
    part2(input)
}

fun part1(input: Pair<Array<BooleanArray>, MutableList<Pair<String, Int>>>): Int {
    return fold(input.first, input.second.first()).sumOf { it.count { b -> b } }
}

fun part2(input: Pair<Array<BooleanArray>, MutableList<Pair<String, Int>>>) {
    var matrix = input.first
    for (instruction in input.second) {
        matrix = fold(matrix, instruction)
    }
    printMatrix(matrix)
}


fun buildMatrix(path: String): Pair<Array<BooleanArray>, MutableList<Pair<String, Int>>> {
    val lines = File(path).readLines()
    val coordinates: MutableList<Pair<Int, Int>> = mutableListOf()
    val instructions: MutableList<Pair<String, Int>> = mutableListOf()
    lines.forEach {
        run {
            if (it.isNotEmpty()) {
                if (it.startsWith("fold along")) {
                    val instruction = it.substringAfter("fold along ").split("=")
                    instructions.add(Pair(instruction[0], instruction[1].toInt()))
                } else {
                    val xy = it.split(",")
                    coordinates.add(Pair(xy[0].toInt(), xy[1].toInt()))
                }
            }
        }
    }

    val sizeX = instructions.first { it.first == "x" }.second * 2 + 1
    val sizeY = instructions.first { it.first == "y" }.second * 2 + 1
    val matrix = Array(sizeY) { _ -> BooleanArray(sizeX) { _ -> false } }

    coordinates.forEach { matrix[it.second][it.first] = true }

    return Pair(matrix, instructions)
}

fun printMatrix(matrix: Array<BooleanArray>) {
    for (j in matrix) {
        for (i in j) {
            if (i) {
                print("#")
            } else {
                print(" ")
            }
        }
        println()
    }
    println()
}

fun fold(matrix: Array<BooleanArray>, instruction: Pair<String, Int>): Array<BooleanArray> {
    var sizeX = if (instruction.first == "x") instruction.second else matrix[0].size
    var sizeY = if (instruction.first == "y") instruction.second else matrix.size
    val newMatrix = Array(sizeY) { _ -> BooleanArray(sizeX) { _ -> false } }
    if (instruction.first == "x") {
        for (i in newMatrix[0].indices) {
            for (j in newMatrix.indices) {
                newMatrix[j][i] = matrix[j][i] || matrix[j][2 * instruction.second - i]
            }
        }
    } else if (instruction.first == "y") {
        for (j in newMatrix.indices) {
            for (i in newMatrix[0].indices) {
                newMatrix[j][i] = matrix[j][i] || matrix[2 * instruction.second - j][i]
            }
        }
    }
    return newMatrix
}