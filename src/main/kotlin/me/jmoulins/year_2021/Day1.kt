package me.jmoulins.year_2021

/**
 * @see https://adventofcode.com/2021/day/1
 */
class Day1 {

    companion object {
        @JvmStatic
        fun main(vararg args: String) {
            val depthsList: List<Int> = readFileUsingGetResource()
            part1(depthsList)
            part2(depthsList)
        }

        // Part 1 - Measurement depth
        private fun part1(depthsList: List<Int>) {
            val count = countLargerMeasurements(depthsList)
            println("There are $count measurements that are larger than the previous measurement")
        }

        // Part 2 - Three-measurement sliding window
        private fun part2(depthsList: List<Int>) {
            val threeMeasurementsDepthsList: MutableList<Int> = mutableListOf()
            for (i in 0 until depthsList.size - 2) {
                threeMeasurementsDepthsList.add(depthsList[i] + depthsList[i + 1] + depthsList[i + 2])
            }
            val count = countLargerMeasurements(threeMeasurementsDepthsList)
            println("There are $count sums that are larger than the previous sum")
        }

        private fun countLargerMeasurements(depthsList: List<Int>): Int {
            var count = 0;
            for (i in 1 until depthsList.size) {
                if (depthsList[i] > depthsList[i - 1]) count += 1
            }
            return count
        }

        private fun readFileUsingGetResource(): List<Int> {
            return this::class.java.getResourceAsStream("/year_2021/day1/input.txt")?.bufferedReader()?.readLines()
                ?.map { it.toInt() } ?: emptyList()
        }
    }
}