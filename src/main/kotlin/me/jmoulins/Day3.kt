package me.jmoulins

/**
 * @see https://adventofcode.com/2021/day/3
 */
class Day3 {

    companion object {
        @JvmStatic
        fun main(vararg args: String) {
            val diagnostic: List<String> = readFileUsingGetResource()
            part1(diagnostic)
            println()
            part2(diagnostic)
        }

        // Part 1 - Binary diagnostic
        private fun part1(diagnostic: List<String>) {
            val powerConsumption = calculatePowerConsumption(diagnostic)
            println("The power consumption is $powerConsumption")
        }

        // Part 2 - Life support rating
        private fun part2(diagnostic: List<String>) {
            val lifeSupportRating = calculateLifeSupportRating(diagnostic)
            println("The life support rating is $lifeSupportRating")
        }

        private fun calculatePowerConsumption(diagnostic: List<String>): Int {
            var gammaRate = 0;
            var epsilonRate = 0;
            if (diagnostic.isNotEmpty()) {
                var gammaRateStr = ""
                for (i in 0 until diagnostic[0].length) {
                    gammaRateStr += (diagnostic.map { it[i] }.groupingBy { it }.eachCount()
                        .maxByOrNull { it.value }?.key)
                }
                val epsilonRateStr = gammaRateStr.replace("0", "x").replace("1", "0").replace("x", "1")
                gammaRate = gammaRateStr.toInt(2)
                epsilonRate = epsilonRateStr.toInt(2)
                println("The gamma rate is $gammaRateStr, in decimal $gammaRate")
                println("The epsilon rate is $epsilonRateStr, in decimal $epsilonRate")

            }
            return gammaRate * epsilonRate
        }

        private fun calculateLifeSupportRating(diagnostic: List<String>): Int {
            var oxygenGeneratorRating = 0
            var co2ScrubberRating = 0

            if (diagnostic.isNotEmpty()) {

                var oxygenGeneratorRatingList = diagnostic.toList()
                var co2ScrubberRatingList = diagnostic.toList()
                var i = 0
                var oxygenGeneratorRatingFound = false
                var co2ScrubberRatingFound = false

                while (i < diagnostic[0].length && (!oxygenGeneratorRatingFound || !co2ScrubberRatingFound)) {

                    if (!oxygenGeneratorRatingFound) {

                        // Find the most common bit in position i. The comparator ensure that 1 is the most common bit in case of equality
                        val mostCommonBit = oxygenGeneratorRatingList.map { it[i] }.groupingBy { it }.eachCount()
                            .toSortedMap(Comparator.reverseOrder()).maxByOrNull { it.value }?.key

                        // Filter the remaining binary strings and keep only the ones containing the most common bit in position i
                        oxygenGeneratorRatingList = oxygenGeneratorRatingList.filter { it[i] == mostCommonBit }

                        // When there is only one element left in the list \o/
                        if (oxygenGeneratorRatingList.size == 1) {
                            oxygenGeneratorRatingFound = true
                            val oxygenGeneratorRatingStr = oxygenGeneratorRatingList[0]
                            oxygenGeneratorRating = oxygenGeneratorRatingStr.toInt(2)
                            println("The oxygen generator rating is $oxygenGeneratorRatingStr, in decimal $oxygenGeneratorRating")
                        }
                    }

                    if (!co2ScrubberRatingFound) {

                        // Find the most common bit in position i. The comparator ensure that 1 is the most common bit in case of equality
                        val mostCommonBit = co2ScrubberRatingList.map { it[i] }.groupingBy { it }.eachCount()
                            .toSortedMap(Comparator.reverseOrder()).maxByOrNull { it.value }?.key

                        // Filter the remaining binary strings and keep only the ones containing the less common bit in position i
                        co2ScrubberRatingList = co2ScrubberRatingList.filter { it[i] != mostCommonBit }

                        // When there is only one element left in the list \o/
                        if (co2ScrubberRatingList.size == 1) {
                            co2ScrubberRatingFound = true
                            val co2ScrubberRatingStr = co2ScrubberRatingList[0]
                            co2ScrubberRating = co2ScrubberRatingStr.toInt(2)
                            println("The CO2 scrubber rating is $co2ScrubberRatingStr, in decimal $co2ScrubberRating")
                        }
                    }
                    i++
                }
            }
            return oxygenGeneratorRating * co2ScrubberRating
        }

        private fun readFileUsingGetResource(): List<String> {
            return this::class.java.getResourceAsStream("/day3/input.txt")?.bufferedReader()?.readLines() ?: emptyList()
        }
    }
}