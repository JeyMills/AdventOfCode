package me.jmoulins.year_2022

fun main() {
    val inputTest = java.io.File("src/main/resources/year_2022/day10/input-test.txt").readLines()
    val input = java.io.File("src/main/resources/year_2022/day10/input.txt").readLines()

    println("Part 1 - Sum of signal strength (test): ${part1(inputTest)}")
    println("Part 1 - Sum of signal strength: ${part1(input)}")


    println("Part 2 - CRT (test):")
    part2(inputTest)
    println("Part 2 - CRT:")
    part2(input)
}

fun part1(input: List<String>): Int {
    var x = 1
    var cycle = 0
    val signalStrength = mutableMapOf<Int, Int>()

    for (i in input) {
        with(i) {
            when {
                startsWith("noop") -> {
                    cycle++
                    if ((cycle - 20) % 40 == 0) signalStrength[cycle] = cycle * x
                }
                startsWith("addx") -> {
                    cycle++
                    if ((cycle - 20) % 40 == 0) signalStrength[cycle] = cycle * x
                    cycle++
                    if ((cycle - 20) % 40 == 0) signalStrength[cycle] = cycle * x
                    x += i.drop(5).toInt()
                }
            }
        }
    }

    return signalStrength.values.sum()
}

fun part2(input: List<String>) {
    var x = 1
    var cycle = 0
    val crt = mutableListOf<Char>()

    for (i in input) {
        with(i) {
            when {
                startsWith("noop") -> {
                    cycle++
                    if ((x - 1) <= (cycle - 1) % 40 && (cycle - 1) % 40 <= (x + 1)) {
                        crt.add('#')
                    } else {
                        crt.add('.')
                    }
                }
                startsWith("addx") -> {
                    cycle++
                    if ((x - 1) <= (cycle - 1) % 40 && (cycle - 1) % 40 <= (x + 1)) {
                        crt.add('#')
                    } else {
                        crt.add('.')
                    }
                    cycle++
                    if ((x - 1) <= (cycle - 1) % 40 && (cycle - 1) % 40 <= (x + 1)) {
                        crt.add('#')
                    } else {
                        crt.add('.')
                    }
                    x += i.drop(5).toInt()
                }
                else -> {}
            }
        }
    }

    crt.chunked(40).forEach { println(it.joinToString("")) }
    println()
}