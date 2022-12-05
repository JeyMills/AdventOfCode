package me.jmoulins.year_2022

import java.io.File

fun main() {
    val input = File("src/main/resources/year_2022/day4/input.txt").readLines()
    val countPart1 = input.count { s ->
        val sections = s.split(",")
        val section1 = sections.first()
        val section1Limits = section1.split("-")
        val range1 = Pair(section1Limits.first().toInt(), section1Limits.last().toInt())
        val section2 = sections.last()
        val section2Limits = section2.split("-")
        val range2 = Pair(section2Limits.first().toInt(), section2Limits.last().toInt())
        (range1.first >= range2.first && range1.second <= range2.second) || (range1.first <= range2.first && range1.second >= range2.second)
    }
    println("Part 1 - Assignment pairs fully contained in another: $countPart1")

    val countPart2 = input.count { s ->
        val sections = s.split(",")
        val section1 = sections.first()
        val section1Limits = section1.split("-")
        val range1 = Pair(section1Limits.first().toInt(), section1Limits.last().toInt())
        val section2 = sections.last()
        val section2Limits = section2.split("-")
        val range2 = Pair(section2Limits.first().toInt(), section2Limits.last().toInt())
        (range1.first >= range2.first && range1.first <= range2.second)
                || (range1.second >= range2.first && range1.second <= range2.second)
                || (range2.first >= range1.first && range2.first <= range1.second)
                || (range2.second >= range1.first && range2.second <= range1.second)
    }
    println("Part 2 - Assignment pairs with overlap: $countPart2")
}