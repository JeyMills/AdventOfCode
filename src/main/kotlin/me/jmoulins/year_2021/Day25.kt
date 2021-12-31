package me.jmoulins.year_2021

import java.io.File

fun main() {
    val seaCucumbersTest = readSeaCucumbersInitialPosition("src/main/resources/year_2021/day25/input-test.txt")
    printSeaCucumbers(seaCucumbersTest)
    println(iterate(seaCucumbersTest))

    val seaCucumbers = readSeaCucumbersInitialPosition("src/main/resources/year_2021/day25/input.txt")
    printSeaCucumbers(seaCucumbers)
    println(iterate(seaCucumbers))
}

fun iterate(seaCucumbers: List<MutableList<Char>>) : Int {
    println("Step 1")
    var current = seaCucumbers
    var next = move(seaCucumbers)
    printSeaCucumbers(next)
    var i = 1
    while (next != current) {
        i++
        println("Step $i")
        val nextTemp = move(next)
        current = next
        next = nextTemp
        printSeaCucumbers(next)
    }
    return i
}

fun move(seaCucumbers: List<MutableList<Char>>): List<MutableList<Char>> {
    // Move east-facing sea cucumbers
    val seaCucumbersAfterEastFacingMove = seaCucumbers.toMutableList().map { it.toMutableList() }
    for (i in seaCucumbers.indices) {
        for (j in seaCucumbers[i].indices) {
            if (seaCucumbers[i][j] == '>' && seaCucumbers[i][(j + 1) % seaCucumbers[i].size] == '.') {
                seaCucumbersAfterEastFacingMove[i][j] = '.'
                seaCucumbersAfterEastFacingMove[i][(j + 1) % seaCucumbers[i].size] = '>'
            }
        }
    }
    // Move south-facing sea cucumbers
    val seaCucumbersAfterSouthFacingMove = seaCucumbersAfterEastFacingMove.toMutableList().map { it.toMutableList() }
    for (i in seaCucumbersAfterEastFacingMove.indices) {
        for (j in seaCucumbersAfterEastFacingMove[i].indices) {
            if (seaCucumbersAfterEastFacingMove[i][j] == 'v' && seaCucumbersAfterEastFacingMove[(i + 1) % seaCucumbersAfterEastFacingMove.size][j] == '.') {
                seaCucumbersAfterSouthFacingMove[i][j] = '.'
                seaCucumbersAfterSouthFacingMove[(i + 1) % seaCucumbersAfterEastFacingMove.size][j] = 'v'
            }
        }
    }
    return seaCucumbersAfterSouthFacingMove
}

fun printSeaCucumbers(seaCucumbers: List<MutableList<Char>>) {
    seaCucumbers.forEach {
        println(it.joinToString(""))
    }
    println()
}

fun readSeaCucumbersInitialPosition(path: String): List<MutableList<Char>> {
    return File(path).readLines().map { it.toMutableList() }
}