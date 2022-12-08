package me.jmoulins.year_2022

import java.io.File
import kotlin.math.max

fun main() {
    val input: Array<IntArray> = File("src/main/resources/year_2022/day8/input.txt")
        .readLines()
        .map { s -> s.chunked(1).map { it.toInt() }.toIntArray() }
        .toTypedArray()

    val count = countVisibleTrees(input)
    println("Part 1 - Trees visible from outside the grid: $count")

    val highestScenicScore = findHighestScenicScore(input)
    println("Part 2 - Highest scenic score: $highestScenicScore")
}

private fun countVisibleTrees(input: Array<IntArray>): Int {
    val edgeMin = 0
    val edgeMax = input.lastIndex

    var count = 0
    for (i in edgeMin..edgeMax) {
        for (j in edgeMin..edgeMax) {
            if (i == edgeMin || i == edgeMax) {
                count++
                continue
            }
            if (j == edgeMin || j == edgeMax) {
                count++
                continue
            }

            // Top
            if (input[i][j] > input[i].filterIndexed { index, _ -> index < j }.maxOrNull()!!) {
                count++
                continue
            }

            // Bottom
            if (input[i][j] > input[i].filterIndexed { index, _ -> index > j }.maxOrNull()!!) {
                count++
                continue
            }

            // Left
            val leftMax = i - 1
            var leftVisible = true
            for (l in edgeMin..leftMax) {
                if (input[i][j] <= input[l][j]) {
                    leftVisible = false
                    continue
                }
            }
            if (leftVisible) {
                count++
                continue
            }

            // Right
            val rightMin = i + 1
            var rightVisible = true
            for (l in rightMin..edgeMax) {
                if (input[i][j] <= input[l][j]) {
                    rightVisible = false
                    continue
                }
            }
            if (rightVisible) {
                count++
                continue
            }
        }
    }
    return count
}

private fun findHighestScenicScore(input: Array<IntArray>): Int {
    val edgeMin = 0
    val edgeMax = input.lastIndex

    var highestScenicScore = 0
    for (i in edgeMin..edgeMax) {
        for (j in edgeMin..edgeMax) {
            // Exclude edges
            if (i == edgeMin || i == edgeMax || j == edgeMin || j == edgeMax) {
                continue
            }

            // Score top
            var topScore = 0
            for (topIdx in j - 1 downTo edgeMin) {
                topScore++
                if (input[i][j] <= input[i][topIdx]) {
                    break
                }
            }

            // Score bottom
            var bottomScore = 0
            for (bottomIdx in (j + 1)..edgeMax) {
                bottomScore++
                if (input[i][j] <= input[i][bottomIdx]) {
                    break
                }
            }

            // Score left
            var leftScore = 0
            for (leftIdx in i - 1 downTo edgeMin) {
                leftScore++
                if (input[i][j] <= input[leftIdx][j]) {
                    break
                }
            }

            // Score right
            var rightScore = 0
            for (rightIdx in (i + 1)..edgeMax) {
                rightScore++
                if (input[i][j] <= input[rightIdx][j]) {
                    break
                }
            }

            highestScenicScore = max(highestScenicScore, topScore * bottomScore * leftScore * rightScore)
        }
    }
    return highestScenicScore
}