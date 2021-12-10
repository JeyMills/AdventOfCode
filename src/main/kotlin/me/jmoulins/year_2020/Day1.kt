package me.jmoulins.year_2020

import java.io.File

fun main() {
    val numbers = File("src/main/resources/year_2020/day1/input.txt")
        .readLines()
        .map(String::toInt)

    println(numbers.findPairOfSum(2020)?.let { (a, b) -> a * b })
    println(numbers.findTripleOfSum(2020)?.let { (a, b, c) -> a * b * c })
}

private fun List<Int>.findPairOfSum(sum: Int): Pair<Int, Int>? {
    val complements = associateBy { sum - it }
    return firstNotNullOfOrNull { number ->
        val complement = complements[number]
        if (complement != null) Pair(number, complement) else null
    }
}

private fun List<Int>.findTripleOfSum(sum: Int): Triple<Int, Int, Int>? =
    firstNotNullOfOrNull { x ->
        findPairOfSum(sum - x)?.let { pair ->
            Triple(x, pair.first, pair.second)
        }
    }
