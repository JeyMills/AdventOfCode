package me.jmoulins

/**
 * @see https://adventofcode.com/2021/day/2
 */
class Day2 {

    companion object {
        @JvmStatic
        fun main(vararg args: String) {
            val commands: List<String> = readFileUsingGetResource()
            part1(commands)
            part2(commands)
        }

        // Part 1 - Dive ! (product between horizontal position and depth)
        private fun part1(commands: List<String>) {
            val product = calculateHorizontalPositionAndDepthProduct(commands)
            println("The product between horizontal position and depth is $product")
        }

        // Part 2 - Dive ! (product between horizontal position and depth calculated with aim)
        private fun part2(commands: List<String>) {
            val product = calculateHorizontalPositionAndDepthProduct(commands, true)
            println("The product between horizontal position and depth calculate with aim is $product")
        }

        private fun calculateHorizontalPositionAndDepthProduct(commands: List<String>, withAim: Boolean = false): Int {
            var horizontalPosition = 0
            var depth = 0
            var aim = 0
            for (command in commands) {
                val split = command.split(" ")
                val action = Action.valueOf(split.first().uppercase())
                val value = split.last().toInt()
                when (action) {
                    Action.FORWARD -> {
                        horizontalPosition += value
                        if (withAim) depth += aim * value
                    }
                    Action.DOWN -> if (withAim) aim += value else depth += value
                    Action.UP -> if (withAim) aim-= value else depth -= value
                }
            }
            return horizontalPosition * depth
        }

        private fun readFileUsingGetResource(): List<String> {
            return this::class.java.getResourceAsStream("/day2/input.txt")?.bufferedReader()?.readLines() ?: emptyList()
        }

        enum class Action {
            FORWARD,
            DOWN,
            UP;
        }
    }
}