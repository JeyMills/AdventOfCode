package me.jmoulins.year_2022

import java.io.File
import java.util.*

fun main() {
    val input = File("src/main/resources/year_2022/day5/input.txt").readLines()
    val (stacks, instructions) = extractStacksAndInstructions(input)
    instructions.forEach {
        for (i in 1..it.first) {
            stacks[it.third - 1].push(stacks[it.second - 1].pop())
        }
    }
    val topPart1 = String(stacks.map { it.peek() }.toCharArray())
    println("Part 1 - Crate on top of stacks: $topPart1")

    val (stacks2, instructions2) = extractStacksAndInstructions(input)
    instructions2.forEach {
        stacks2[it.third - 1].addAll(stacks2[it.second - 1].takeLast(it.first))
        for (i in 1..it.first) {
            stacks2[it.second - 1].pop()
        }
    }
    val topPart2 = String(stacks2.map { it.peek() }.toCharArray())
    println("Part 2 - Crate on top of stacks: $topPart2")
}

fun extractStacksAndInstructions(input: List<String>): Pair<List<Stack<Char>>, List<Triple<Int, Int, Int>>> {
    val stacks = MutableList(9) { Stack<Char>() }
    input.take(8).forEach { it ->
        it.chunked(4).forEachIndexed { index, s ->
            if (s.isNotBlank()) {
                stacks[index].add(0, s[1])
            }
        }
    }

    val regex = """move (\d+) from (\d+) to (\d+)""".toRegex()
    val instructions = input.drop(10).map {
        val (move, from, to) = regex.find(it)!!.destructured
        Triple(move.toInt(), from.toInt(), to.toInt())
    }

    return stacks.toList() to instructions
}