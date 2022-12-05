package me.jmoulins.year_2022

import java.io.File

/**
 * Part 1
 * A / X = rock => 1pt
 * B / Y = paper => 2pts
 * C / Z = scissors => 3pts
 *
 * Win => 6pts
 * Draw => 3pts
 * Loss => 0pt
 *
 * A X = 1 + 3 = 4pts
 * A Y = 2 + 6 = 8pts
 * A Z = 3 + 0 = 3pts
 * B X = 1 + 0 = 1pt
 * B Y = 2 + 3 = 5pts
 * B Z = 3 + 6 = 9pts
 * C X = 1 + 6 = 7pts
 * C Y = 2 + 0 = 2pts
 * C Z = 3 + 3 = 6pts
 *
 * Part 2
 * A = rock => 1pt
 * B = paper => 2pts
 * C = scissors => 3pts
 *
 * X = loss => 0pt
 * Y = draw => 3pts
 * Z = win => 6pts
 *
 * A X = rock / loss = rock / scissors = 3 + 0 = 3pts
 * A Y = rock / draw = rock / rock = 1 + 3 = 4pts
 * A Z = rock / win = rock / paper = 2 + 6 = 8pts
 * B X = paper / loss = paper / rock = 1 + 0 = 1pt
 * B Y = paper / draw = paper / paper = 2 + 3 = 5pts
 * B Z = paper / win = paper / scissors = 3 + 6 = 9pts
 * C X = scissors / loss = scissors / paper = 2 + 0 = 2pts
 * C Y = scissors / draw = scissors / scissors = 3 + 3 = 6pts
 * C Z = scissors / win = scissors / rock = 1 + 6 = 7pts
 *
 */

fun main() {
    val part1Points = mapOf(
        "A X" to 4,
        "A Y" to 8,
        "A Z" to 3,
        "B X" to 1,
        "B Y" to 5,
        "B Z" to 9,
        "C X" to 7,
        "C Y" to 2,
        "C Z" to 6
    )
    val input = File("src/main/resources/year_2022/day2/input.txt").readLines()
    val totalPart1 = input.sumOf { part1Points[it]!! }
    println("Part 1 : total score if everything goes exactly according to your strategy guide: $totalPart1")

    val part2Points = mapOf(
        "A X" to 3,
        "A Y" to 4,
        "A Z" to 8,
        "B X" to 1,
        "B Y" to 5,
        "B Z" to 9,
        "C X" to 2,
        "C Y" to 6,
        "C Z" to 7
    )
    val totalPart2 = input.sumOf { part2Points[it]!! }
    println("Part 2 : total score if everything goes exactly according to your strategy guide: $totalPart2")
}