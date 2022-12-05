package me.jmoulins.year_2022

import java.io.File

fun main() {
    val input = File("src/main/resources/year_2022/day3/input.txt").readLines()
    val sumOfPrioritiesPart1 = input.sumOf { findCommonChars(it.take(it.length / 2), it.takeLast(it.length / 2)) }

    println("Part 1 - The sum of the priorities of those item types is: $sumOfPrioritiesPart1")

    val sumOfPrioritiesPart2 = input.chunked(3).sumOf { findCommonChars(it.first(), it[1], it.last()) }
    println("Part 2 - The sum of the priorities of those item types is: $sumOfPrioritiesPart2")
}

fun findCommonChars(s1: String, s2: String): Int {
    // a = 97 -> z = 122
    // A = 65 -> Z = 90

    for (ch in s1.iterator()) {
        if (s2.contains(ch)) {
            return if (ch.code > 96) {
                ch.code - 96
            } else {
                ch.code - 38
            }

        }
    }
    return 0
}

fun findCommonChars(s1: String, s2: String, s3: String): Int {
    val chars = mutableListOf<Char>()
    for (ch in s1.iterator()) {
        if (s2.contains(ch)) {
            chars.add(ch)
        }
    }
    for (ch in chars) {
        if (s3.contains(ch)) {
            return if (ch.code > 96) {
                ch.code - 96
            } else {
                ch.code - 38
            }
        }
    }
    return 0
}