package me.jmoulins.year_2021

import java.io.File
import java.math.BigInteger

fun main() {
    val start = System.currentTimeMillis()
    val inputTest = buildPolymerInput("src/main/resources/year_2021/day14/input-test.txt")
    val input = buildPolymerInput("src/main/resources/year_2021/day14/input.txt")

    println("PART 1")
    iterate(inputTest, 10)
    println()
    iterate(input, 10)
    println()

    println("PART 2")
    iterate(inputTest, 40)
    println()
    iterate(input, 40)
    println()

    println("Execution time: ${System.currentTimeMillis() - start} ms")
}

fun buildPolymerInput(path: String): Pair<String, HashMap<String, String>> {
    val lines = File(path).readLines()
    val polymerTemplate: String = lines.first()
    val rules = HashMap<String, String>()
    for (i in 2 until lines.size) {
        val line = lines[i].split(" -> ")
        rules[line.first()] = line.last()
    }
    return Pair(polymerTemplate, rules)
}

fun iterate(input: Pair<String, HashMap<String, String>>, steps: Int) {
    val eachCountPairs = input.first.windowed(2).groupingBy { it }.eachCount()
    val countPairs: MutableMap<String, BigInteger> = mutableMapOf()
    eachCountPairs.forEach { countPairs[it.key] = it.value.toBigInteger() }

    val eachCountLetters = input.first.chunked(1).groupingBy { it }.eachCount()
    val countLetters: MutableMap<String, BigInteger> = mutableMapOf()
    eachCountLetters.forEach { countLetters[it.key] = it.value.toBigInteger() }

    for (i in 1..steps) {
        val previousCountPairs =  countPairs.toMutableMap()
        for (rule in input.second) {
            val previousPair = rule.key
            val newPair1 = rule.key.first() + rule.value
            val newPair2 = rule.value + rule.key.last()
            val nbPreviousPair = previousCountPairs[previousPair] ?: BigInteger.ZERO
            countPairs[previousPair] = countPairs[previousPair]?.minus(nbPreviousPair) ?: BigInteger.ZERO
            countPairs[newPair1] = (countPairs[newPair1] ?: BigInteger.ZERO) + nbPreviousPair
            countPairs[newPair2] = (countPairs[newPair2] ?: BigInteger.ZERO) + nbPreviousPair
            countLetters[rule.value] = countLetters[rule.value]?.plus(nbPreviousPair) ?: nbPreviousPair
        }
    }

    val max: Map.Entry<String, BigInteger>? = countLetters.maxByOrNull { it.value }
    println("Most common element after $steps steps is ${max?.key}: ${max?.value}")
    val min: Map.Entry<String, BigInteger>? = countLetters.minByOrNull { it.value }
    println("Less common element after $steps steps is ${min?.key}: ${min?.value}")
    val result = max!!.value - min!!.value
    println("Result after $steps steps is $result")
}