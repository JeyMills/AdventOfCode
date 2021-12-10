package me.jmoulins.year_2021

import java.io.File
import java.lang.IllegalArgumentException
import java.math.BigInteger

fun main() {
    val navigationLinesTest = readFile("src/main/resources/year_2021/day10/input-test.txt")
    val navigationLines = readFile("src/main/resources/year_2021/day10/input.txt")

    println("PART 1")
    println("The total syntax error score for those errors is for test input ${part1(navigationLinesTest)}")
    println("The total syntax error score for those errors is for puzzle input ${part1(navigationLines)}")

    println("PART 2")
    println("The middle score for test input is ${part2(navigationLinesTest)}")
    println("The middle score for puzzle input is ${part2(navigationLines)}")
}

fun readFile(path: String): List<NavigationLine> {
    val navigationLines = File(path)
        .readLines()
        .map(NavigationLine::parse)
    navigationLines.forEach(NavigationLine::computeStatus)
    return navigationLines
}

fun part1(navigationLines: List<NavigationLine>): BigInteger {
    return navigationLines
        .filter { it.status == NavigationLine.NavigationLineStatus.CORRUPTED }
        .sumOf { it.corruptedCharacter?.corruptionPoints ?: BigInteger.ZERO }
}

fun part2(navigationLines: List<NavigationLine>): BigInteger {
    val scores = navigationLines
        .filter { it.status == NavigationLine.NavigationLineStatus.INCOMPLETE }
        .map {
            it.unclosedCharacters
                ?.map { character -> character.pair }
                ?.reversed()
                ?.map { character -> character!!.completionPoints }
        }
        .map {
            it!!.reduce { acc: BigInteger, i: BigInteger -> acc * 5.toBigInteger() + i }
        }
        .sorted()

    return scores[scores.size / 2]
}

data class Character(
    val char: Char,
    val corruptionPoints: BigInteger = BigInteger.ZERO,
    val completionPoints: BigInteger = BigInteger.ZERO,
    val action: CharacterAction,
    val pair: Character?,
) {
    companion object {
        fun parse(input: Char) = Character(
            char = input,
            corruptionPoints = when (input) {
                ')' -> 3.toBigInteger()
                ']' -> 57.toBigInteger()
                '}' -> 1197.toBigInteger()
                '>' -> 25137.toBigInteger()
                else -> BigInteger.ZERO
            },
            completionPoints = when (input) {
                ')' -> 1.toBigInteger()
                ']' -> 2.toBigInteger()
                '}' -> 3.toBigInteger()
                '>' -> 4.toBigInteger()
                else -> BigInteger.ZERO
            },
            action = when (input) {
                ')', ']', '}', '>' -> CharacterAction.CLOSE
                '(', '[', '{', '<' -> CharacterAction.OPEN
                else -> throw IllegalArgumentException()
            },
            pair = when (input) {
                '(' -> Character(')', 3.toBigInteger(), 1.toBigInteger(), CharacterAction.CLOSE, null)
                '[' -> Character(']', 57.toBigInteger(), 2.toBigInteger(), CharacterAction.CLOSE, null)
                '{' -> Character('}', 1197.toBigInteger(), 3.toBigInteger(), CharacterAction.CLOSE, null)
                '<' -> Character('>', 25137.toBigInteger(), 4.toBigInteger(), CharacterAction.CLOSE, null)
                ')', ']', '}', '>' -> null
                else -> throw IllegalArgumentException()
            }
        )
    }

    enum class CharacterAction {
        OPEN,
        CLOSE
    }
}

data class NavigationLine(
    val characters: List<Character>,
) {
    lateinit var status: NavigationLineStatus
    var corruptedCharacter: Character? = null
    var unclosedCharacters: List<Character>? = emptyList()

    fun computeStatus() {
        val unclosedCharacters = mutableListOf<Character>()
        characters.forEach {
            val previousCharacter =
                if (unclosedCharacters.size != 0) unclosedCharacters[unclosedCharacters.size - 1] else null
            if (previousCharacter == null) {
                if (it.action == Character.CharacterAction.CLOSE) {
                    status = NavigationLineStatus.CORRUPTED
                    corruptedCharacter = it
                    return
                } else {
                    unclosedCharacters.add(it)
                }
            } else {
                if (it.action == Character.CharacterAction.CLOSE) {
                    if (previousCharacter.pair?.char == it.char) {
                        unclosedCharacters.removeAt(unclosedCharacters.size - 1)
                        return@forEach
                    } else {
                        status = NavigationLineStatus.CORRUPTED
                        corruptedCharacter = it
                        return
                    }
                } else {
                    unclosedCharacters.add(it)
                }
            }
        }
        this.unclosedCharacters = unclosedCharacters
        status = if (unclosedCharacters.isEmpty()) NavigationLineStatus.VALID else NavigationLineStatus.INCOMPLETE
    }

    companion object {
        fun parse(string: String) = NavigationLine(
            characters = string.toCharArray().map { Character.parse(it) }
        )
    }

    enum class NavigationLineStatus {
        VALID,
        INCOMPLETE,
        CORRUPTED
    }
}