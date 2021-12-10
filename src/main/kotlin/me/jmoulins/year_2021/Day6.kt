package me.jmoulins.year_2021

import java.math.BigInteger

/**
 * @see https://adventofcode.com/2021/day/6
 */
class Day6 {
    companion object {
        @JvmStatic
        fun main(vararg args: String) {
            val lines: List<Int> = readFile()

            calculateNumberOfLanternfishAfterNIterations(80, lines)
            calculateNumberOfLanternfishAfterNIterations(256, lines)
        }

        private fun calculateNumberOfLanternfishAfterNIterations(iterations: Short, lines: List<Int>) {
            val lanternfishShoal = LanternfishShoal(lines)
            for (i in 0 until iterations) {
                lanternfishShoal.anotherDay()
            }
            println("Number of lanterfishes after $iterations days: ${lanternfishShoal.count()}")
        }

        private fun readFile(): List<Int> {
            return this::class.java.getResourceAsStream("/year_2021/day6/input.txt")?.bufferedReader()?.readLines()?.get(0)
                ?.split(',')?.map { it.toInt() }.orEmpty()
        }

        class LanternfishShoal(initialData: List<Int>) {
            var shoal = Array<BigInteger>(9) { BigInteger.ZERO }

            init {
                initialData.forEach { shoal[it]++ }
            }

            fun anotherDay() {
                val newFishes = shoal[0]
                for (i in 1..8) {
                    shoal[i - 1] = shoal[i]
                }
                shoal[6] += newFishes
                shoal[8] = newFishes
            }

            fun count(): BigInteger {
                return shoal.sumOf { it }
            }
        }
    }
}