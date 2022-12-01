package me.jmoulins.year_2022

import java.io.File

fun main() {
    val elves = mutableListOf<Int>()
    var calories = 0
    File("src/main/resources/year_2022/day1/input.txt").readLines().forEach {
        run {
            if (it.isNotEmpty()) {
                calories += it.toInt()
            } else {
                elves.add(calories)
                calories = 0
            }
        }
    }
    val maxOf = elves.maxOf { it }
    println("Max calories carried by an elf: $maxOf")

    val sumTop3 = elves.sortedDescending().take(3).reduce { acc, i -> acc + i }
    println("Sum of top 3 max calories carried by an elf: $sumTop3")
}