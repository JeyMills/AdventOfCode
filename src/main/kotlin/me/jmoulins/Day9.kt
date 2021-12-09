package me.jmoulins

/**
 * @see https://adventofcode.com/2021/day/9
 */
class Day9 {

    companion object {
        @JvmStatic
        fun main(vararg args: String) {
            val data = readFile()

            step1(data)
            step2(data)
        }

        private fun step1(data: Array<out IntArray>) {
            println("STEP 1")
            var riskLevel = 0
            for (i in data.indices) {
                val ints = data[i]
                for (j in ints.indices) {
                    if (isLowPoint(i, ints, j, data)) {
                        riskLevel += ints[j] + 1
                    }
                }
            }
            println("Risk level is $riskLevel")
            println()
        }

        private fun step2(data: Array<out IntArray>) {
            println("STEP 2")
            val basinSizes = mutableListOf<Int>()
            val partOfBasins = mutableListOf<Pair<Int, Int>>()
            for (i in data.indices) {
                val ints = data[i]
                for (j in ints.indices) {
                    if (isLowPoint(i, ints, j, data)) {
                        basinSizes.add(calculateBasinSize(i, ints, j, data, partOfBasins))
                    }
                }
            }
            basinSizes.sortDescending()
            val result = basinSizes.take(3).reduce { acc: Int, size: Int -> acc * size }
            println("The product of the three largest sizes is $result")
        }

        private fun isLowPoint(i: Int, ints: IntArray, j: Int, data: Array<out IntArray>): Boolean {
            var isLowPoint = true
            if (i != 0) {
                isLowPoint = isLowPoint && ints[j] < data[i - 1][j]
            }
            if (i != data.size - 1) {
                isLowPoint = isLowPoint && ints[j] < data[i + 1][j]
            }
            if (j != 0) {
                isLowPoint = isLowPoint && ints[j] < data[i][j - 1]
            }
            if (j != ints.size - 1) {
                isLowPoint = isLowPoint && ints[j] < data[i][j + 1]
            }
            return isLowPoint
        }

        private fun calculateBasinSize(
            i: Int,
            ints: IntArray,
            j: Int,
            data: Array<out IntArray>,
            partOfBasins: MutableList<Pair<Int, Int>>
        ): Int {
            var size = 0
            var lookAround = mutableListOf(Pair(i, j))
            partOfBasins.add(Pair(i, j))
            while (lookAround.isNotEmpty()) {
                val tmpList = mutableListOf<Pair<Int, Int>>()
                for (pair in lookAround) {

                    if (pair.first != 0 && data[pair.first - 1][pair.second] != 9 &&
                        !partOfBasins.contains(Pair(pair.first - 1, pair.second))
                    ) {
                        tmpList.add(Pair(pair.first - 1, pair.second))
                        partOfBasins.add(Pair(pair.first - 1, pair.second))
                    }

                    if (pair.first != data.size - 1 && data[pair.first + 1][pair.second] != 9 &&
                        !partOfBasins.contains(Pair(pair.first + 1, pair.second))
                    ) {
                        tmpList.add(Pair(pair.first + 1, pair.second))
                        partOfBasins.add(Pair(pair.first + 1, pair.second))
                    }

                    if (pair.second != 0 && data[pair.first][pair.second - 1] != 9 &&
                        !partOfBasins.contains(Pair(pair.first, pair.second - 1))
                    ) {
                        tmpList.add(Pair(pair.first, pair.second - 1))
                        partOfBasins.add(Pair(pair.first, pair.second - 1))
                    }

                    if (pair.second != ints.size - 1 && data[pair.first][pair.second + 1] != 9 &&
                        !partOfBasins.contains(Pair(pair.first, pair.second + 1))
                    ) {
                        tmpList.add(Pair(pair.first, pair.second + 1))
                        partOfBasins.add(Pair(pair.first, pair.second + 1))
                    }

                }
                size += lookAround.size
                lookAround = tmpList
            }
            return size
        }

        private fun readFile(): Array<out IntArray> {
            return this::class.java.getResourceAsStream("/day9/input.txt")?.bufferedReader()?.readLines()
                ?.map { s -> s.chunked(1).map { it.toInt() }.toIntArray() }!!.toTypedArray()
        }
    }
}