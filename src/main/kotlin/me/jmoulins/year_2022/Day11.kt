package me.jmoulins.year_2022

private const val biggest: Long =  2 * 3 * 5 * 7 * 11 * 13 * 17 * 19

fun main() {
    val inputTest = java.io.File("src/main/resources/year_2022/day11/input-test.txt").readLines()
    val input = java.io.File("src/main/resources/year_2022/day11/input.txt").readLines()

    part1(inputTest, input)
    part2(inputTest, input)

}

private fun parseInput(input: List<String>): List<Monkey> {
    val chunked: List<List<String>> = input.chunked(7)
    val monkeys = List(chunked.size) { Monkey() }
    var count = -1
    chunked.forEach { line ->
        count++
        monkeys[count].items = line[1].drop(18).split(",").map { it.trim().toLong() }.toMutableList()
        monkeys[count].operation = line[2].drop(23)
        monkeys[count].divisibilityTestBy = line[3].drop(21).toInt()
        monkeys[count].positiveMonkey = monkeys[line[4].drop(29).toInt()]
        monkeys[count].negativeMonkey = monkeys[line[5].drop(30).toInt()]
    }
    return monkeys
}

private fun part1(inputTest: List<String>, input: List<String>) {
    val monkeysTest = parseInput(inputTest)
    val monkeys = parseInput(input)

    for (round in 1..20) {
        monkeysTest.forEach { it.inspection(true) }
        monkeys.forEach { it.inspection(true) }
    }

    monkeysTest.forEachIndexed { index, monkey -> monkey.printCounter(index) }
    println(
        "Part 1 - level of monkey business after 20 rounds of stuff-slinging simian shenanigans (test): ${
            monkeysTest.map { it.counter }.sortedDescending().take(2).reduce { acc, i -> acc * i }
        }"
    )
    println()

    monkeys.forEachIndexed { index, monkey -> monkey.printCounter(index) }
    println(
        "Part 1 - level of monkey business after 20 rounds of stuff-slinging simian shenanigans: ${
            monkeys.map { it.counter }.sortedDescending().take(2).reduce { acc, i -> acc * i }
        }"
    )
    println()
}

private fun part2(inputTest: List<String>, input: List<String>) {
    val monkeysTest = parseInput(inputTest)
    val monkeys = parseInput(input)

    for (round in 1..10000) {
        monkeysTest.forEach { it.inspection(false) }
        monkeys.forEach { it.inspection(false) }
        if (round == 1 || round == 20 || round % 1000 == 0) {
            println("== After round $round ==")
            monkeysTest.forEachIndexed { index, monkey -> monkey.printCounter(index) }
            println()
        }
    }

    monkeysTest.forEachIndexed { index, monkey -> monkey.printCounter(index) }
    println(
        "Part 2 - level of monkey business after 10000 rounds of stuff-slinging simian shenanigans (test): ${
            monkeysTest.map { it.counter }.sortedDescending().take(2).reduce { acc, i -> acc * i }
        }"
    )
    println()

    monkeys.forEachIndexed { index, monkey -> monkey.printCounter(index) }
    println(
        "Part 2 - level of monkey business after 10000 rounds of stuff-slinging simian shenanigans: ${
            monkeys.map { it.counter }.sortedDescending().take(2).reduce { acc, i -> acc * i }
        }"
    )
    println()
}

class Monkey(
    var items: MutableList<Long> = mutableListOf(),
    var operation: String = "",
    var divisibilityTestBy: Int = 0,
    var positiveMonkey: Monkey? = null,
    var negativeMonkey: Monkey? = null,
    var counter: Long = 0
) {
    fun inspection(withDivisionOfWorryLevel: Boolean) {
        for (item in items) {
            val splitOperation = operation.split(" ")
            val member = when (splitOperation.last()) {
                "old" -> {
                    item
                }
                else -> {
                    splitOperation.last().toLong()
                }
            }
            var worryLevel: Long = when (splitOperation.first()) {
                "*" -> item * member
                else -> item + member
            }
            if (withDivisionOfWorryLevel) worryLevel = worryLevel.floorDiv(3) else worryLevel %= biggest
            if (worryLevel % divisibilityTestBy == 0L) {
                positiveMonkey?.items?.add(worryLevel)
            } else {
                negativeMonkey?.items?.add(worryLevel)
            }
            counter++
        }
        items.clear()
    }

    fun printCounter(i: Int) {
        println("Monkey $i inspected items $counter time(s).")
    }
}