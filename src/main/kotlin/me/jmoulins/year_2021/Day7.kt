package me.jmoulins.year_2021

import kotlin.math.abs

/**
 * @see https://adventofcode.com/2021/day/7
 */
class Day7 {

    companion object {
        @JvmStatic
        fun main(vararg args: String) {
            val initialHorizontalPositions: List<Int> = readFile()

            step1(initialHorizontalPositions)
            step2(initialHorizontalPositions)
        }

        private fun step1(initialHorizontalPositions: List<Int>) {
            println("STEP 1")
            val minHorizontalPosition = initialHorizontalPositions.minOrNull()!!;
            val maxHorizontalPosition = initialHorizontalPositions.maxOrNull()!!;
            println("Min horizontal position is $minHorizontalPosition")
            println("Max horizontal position is $maxHorizontalPosition")

            val fuelSpent = Array(maxHorizontalPosition + 1) { 0 }
            for (i in minHorizontalPosition..maxHorizontalPosition) {
                for (j in initialHorizontalPositions) {
                    fuelSpent[i] += abs(i - j)
                }
            }
            val min: IndexedValue<Int> = fuelSpent.withIndex().minByOrNull { indexedValue -> indexedValue.value }!!
            println("The horizontal position that the crabs need to use to spend least fuel possible is ${min.index}.")
            println("They spend ${min.value} fuel to align to that position")
            println()
        }

        private fun step2(initialHorizontalPositions: List<Int>) {
            println("STEP 2")
            val minHorizontalPosition = initialHorizontalPositions.minOrNull()!!;
            val maxHorizontalPosition = initialHorizontalPositions.maxOrNull()!!;
            println("Min horizontal position is $minHorizontalPosition")
            println("Max horizontal position is $maxHorizontalPosition")

            val fuelSpent = Array(maxHorizontalPosition + 1) { 0 }
            for (i in minHorizontalPosition..maxHorizontalPosition) {
                for (j in initialHorizontalPositions) {
                    val n = abs(i - j)
                    fuelSpent[i] += n * (n + 1) / 2
                }
            }
            val min: IndexedValue<Int> = fuelSpent.withIndex().minByOrNull { indexedValue -> indexedValue.value }!!
            println("The horizontal position that the crabs need to use to spend least fuel possible is ${min.index}.")
            println("They spend ${min.value} fuel to align to that position")
            println()
        }

        private fun readFile(): List<Int> {
            return this::class.java.getResourceAsStream("/year_2021/day7/input.txt")?.bufferedReader()?.readLines()?.get(0)
                ?.split(',')?.map { it.toInt() }.orEmpty()
        }
    }
}