package me.jmoulins

import java.util.AbstractMap

/**
 * @see https://adventofcode.com/2021/day/4
 */
class Day4 {

    companion object {
        @JvmStatic
        fun main(vararg args: String) {
            val (drawNumbers, bingoGrids) = readFile()

            step1(drawNumbers, bingoGrids)
            step2(drawNumbers, bingoGrids)
        }

        // Part 1 - Giant squid - Winning grid
        private fun step1(drawNumbers: List<Int>, bingoGrids: List<BingoGrid>) {
            var winner: BingoGrid? = null
            var lastDrawNumber = 0
            forLoop@ for (draw in drawNumbers) {
                run forEachLoop@{
                    bingoGrids.forEach {
                        it.draw(draw)
                        if (it.wins()) {
                            winner = it
                            lastDrawNumber = draw
                            println()
                            println("We have a winner: ")
                            it.printGrind()
                            return@forEachLoop
                        }
                    }
                }
                if (winner != null) break@forLoop
            }
            val sumOfAllUnmarkedNumbers = winner!!.sumOfAllUnmarkedNumbers()
            println("The sum of all unmarked numbers is $sumOfAllUnmarkedNumbers")
            println("The last number draws is $lastDrawNumber")
            println("The final score is ${sumOfAllUnmarkedNumbers * lastDrawNumber}")
        }

        // Part 2 - Giant squid - Losing grid
        private fun step2(drawNumbers: List<Int>, bingoGrids: List<BingoGrid>) {
            var loser: BingoGrid? = null
            var lastDrawNumber = 0
            val winningGrids = arrayListOf<BingoGrid>()
            val remainingBingoGrids = bingoGrids.toMutableList()
            forLoop@ for (draw in drawNumbers) {
                run forEachLoop@{
                    remainingBingoGrids.forEach {
                        it.draw(draw)
                        if (it.wins()) {
                            winningGrids.add(it)
                            if (winningGrids.size == bingoGrids.size) {
                                loser = it
                                lastDrawNumber = draw
                                return@forEachLoop
                            }
                        }
                    }
                }
                remainingBingoGrids.removeAll(winningGrids)
                if (loser != null) break@forLoop
            }
            println()
            println("We have a loser: ")
            loser!!.printGrind()
            val sumOfAllUnmarkedNumbers = loser!!.sumOfAllUnmarkedNumbers()
            println("The sum of all unmarked numbers is $sumOfAllUnmarkedNumbers")
            println("The last number draws is $lastDrawNumber")
            println("The final score is ${sumOfAllUnmarkedNumbers * lastDrawNumber}")
        }

        private fun readFile(): Pair<List<Int>, List<BingoGrid>> {
            val lines = this::class.java.getResourceAsStream("/day4/input.txt")?.bufferedReader()?.readLines()
            if (lines == null || lines.isEmpty()) {
                throw Exception()
            }

            // List
            val numbersDrawn = lines[0].split(",").map { it.toInt() }
            var lineIndex = 2


            val bingoGrids: ArrayList<BingoGrid> = arrayListOf()
            var y = 0
            var rows = arrayOfNulls<Array<AbstractMap.SimpleEntry<Int, Boolean>>>(5)

            while (lineIndex < lines.size) {
                val line = lines[lineIndex]
                if (line.isNotEmpty()) {
                    rows[y] = line.trim().split("\\s+".toRegex()).map { AbstractMap.SimpleEntry(it.toInt(), false) }
                        .toTypedArray()
                    y++
                } else {
                    bingoGrids.add(BingoGrid(rows.requireNoNulls()))
                    y = 0
                    rows = arrayOfNulls(5)
                }
                lineIndex++
            }
            return Pair(numbersDrawn, bingoGrids)
        }
    }

    class BingoGrid(private var rows: Array<Array<AbstractMap.SimpleEntry<Int, Boolean>>>) {

        fun draw(number: Int) {
            for (row in rows) {
                for (map in row) {
                    if (map.key == number) {
                        map.setValue(true)
                        return
                    }
                }
            }

        }

        private fun getColumns(): Array<Array<AbstractMap.SimpleEntry<Int, Boolean>>?> {
            val columns: Array<Array<AbstractMap.SimpleEntry<Int, Boolean>>?> = Array(5) {
                Array(5) {
                    AbstractMap.SimpleEntry(0, false)
                }
            }
            for (y in 0 until 5) {
                for (x in 0 until 5) {
                    columns[y]?.set(x, rows[x][y])
                }
            }
            return columns
        }

        fun wins(): Boolean {
            return wins(rows) || wins(getColumns().requireNoNulls())
        }

        private fun wins(data: Array<Array<AbstractMap.SimpleEntry<Int, Boolean>>>): Boolean {
            var wins = false
            for (i in data.indices) {
                wins = wins || data[i].map { mutableEntry -> mutableEntry.value }.reduce { acc, b -> acc && b }
            }
            return wins
        }

        fun printGrind() {
            for (x in 0 until 5) {
                for (y in 0 until 5) {
                    if (rows[x][y].value) {
                        print("${rows[x][y].key}*\t")
                    } else {
                        print("${rows[x][y].key}\t")
                    }
                }
                println()
            }
            println()
        }

        fun sumOfAllUnmarkedNumbers(): Int {
            return rows.sumOf { arrayOfSimpleEntries: Array<AbstractMap.SimpleEntry<Int, Boolean>> ->
                arrayOfSimpleEntries.sumOf { simpleEntry: AbstractMap.SimpleEntry<Int, Boolean> -> if (!simpleEntry.value) simpleEntry.key else 0 }
            }
        }
    }
}