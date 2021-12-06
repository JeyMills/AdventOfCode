package me.jmoulins

/**
 * @see https://adventofcode.com/2021/day/5
 */
class Day5 {

    companion object {
        @JvmStatic
        fun main(vararg args: String) {
            val lines: List<Line> = readFile()

            step1(lines)
            step2(lines)
        }

        // Part 1 - Hydrothermal Venture - horizontal or vertical lines
        private fun step1(lines: List<Line>) {
            val horizontalOrVerticalLines = lines.filter { it.isHorizontalOrVertical() }
            val diagram = Diagram(1000)
            horizontalOrVerticalLines.forEach { diagram.drawLine(it) }
            println("The number of points where at least two lines overlap is ${diagram.countLinesOverlap()}")
        }

        // Part 2 - Hydrothermal Venture - horizontal, vertical, or diagonal lines
        private fun step2(lines: List<Line>) {
            val diagram = Diagram(1000)
            lines.forEach { diagram.drawLine(it) }
            println("The number of points where at least two lines overlap is ${diagram.countLinesOverlap()}")
        }

        private fun readFile(): List<Line> {
            return this::class.java.getResourceAsStream("/day5/input.txt")?.bufferedReader()?.readLines()
                ?.map { it.toLine() }.orEmpty()
        }

        class Line(var x1: Int, var y1: Int, var x2: Int, var y2: Int) {
            fun isHorizontalOrVertical(): Boolean = isHorizontal() || isVertical()

            fun isHorizontal(): Boolean = x1 == x2

            fun isVertical(): Boolean = y1 == y2

            fun getOperation(): Operation {
                return when {
                    x1 < x2 && y1 < y2 -> Operation.X_PLUS_Y_PLUS
                    x1 < x2 && y1 > y2 -> Operation.X_PLUS_Y_MINUS
                    x1 > x2 && y1 < y2 -> Operation.X_MINUS_Y_PLUS
                    x1 > x2 && y1 > y2 -> Operation.X_MINUS_Y_MINUS
                    else -> {
                        throw Exception()
                    }
                }
            }
        }

        class Diagram(var size: Int) {
            var rows = Array(size) { IntArray(size) }

            fun printDiagram() {
                for (y in 0 until size) {
                    for (x in 0 until size) {
                        print("${rows[x][y]}\t")
                    }
                    println()
                }
                println()
            }

            fun drawLine(line: Line) {
                when {
                    line.isHorizontal() -> {
                        val y1 = if (line.y1 < line.y2) line.y1 else line.y2
                        val y2 = if (line.y1 < line.y2) line.y2 else line.y1
                        for (y in y1..y2) {
                            this.rows[line.x1][y] += 1
                        }
                    }
                    line.isVertical() -> {
                        val x1 = if (line.x1 < line.x2) line.x1 else line.x2
                        val x2 = if (line.x1 < line.x2) line.x2 else line.x1
                        for (x in x1..x2) {
                            this.rows[x][line.y1] += 1
                        }
                    }
                    else -> {
                        when (line.getOperation()) {
                            Operation.X_PLUS_Y_PLUS -> {
                                var y = line.y1
                                for (x in line.x1..line.x2) {
                                    this.rows[x][y++] += 1
                                }
                            }
                            Operation.X_PLUS_Y_MINUS -> {
                                var y = line.y1
                                for (x in line.x1..line.x2) {
                                    this.rows[x][y--] += 1
                                }
                            }
                            Operation.X_MINUS_Y_PLUS -> {
                                var y = line.y1
                                for (x in line.x1 downTo line.x2) {
                                    this.rows[x][y++] += 1
                                }
                            }
                            Operation.X_MINUS_Y_MINUS -> {
                                var y = line.y1
                                for (x in line.x1 downTo line.x2) {
                                    this.rows[x][y--] += 1
                                }
                            }
                        }
                    }
                }
            }

            fun countLinesOverlap(): Int {
                return rows.sumOf { it.count { i -> i >= 2 } }
            }
        }

        enum class Operation {
            X_PLUS_Y_PLUS,
            X_MINUS_Y_PLUS,
            X_PLUS_Y_MINUS,
            X_MINUS_Y_MINUS
        }

        private fun String.toLine(): Line {
            val coordinates: List<Int> = this.replace(" -> ", ",").split(",").map { s: String -> s.toInt() }
            return Line(coordinates[0], coordinates[1], coordinates[2], coordinates[3])
        }
    }
}