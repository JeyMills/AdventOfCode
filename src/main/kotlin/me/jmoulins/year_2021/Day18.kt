package me.jmoulins.year_2021

import java.io.File
import kotlin.math.ceil
import kotlin.math.floor

fun main() {
    val inputTest = File("src/main/resources/year_2021/day18/input-test.txt").readLines()
    val input = File("src/main/resources/year_2021/day18/input.txt").readLines()
    val listTest: List<SnailfishNumber> = inputTest.map { SnailfishNumber.parse(it) }
    val list: List<SnailfishNumber> = input.map { SnailfishNumber.parse(it) }

    // Part 1
    println("Part 1")
    part1(listTest)
    part1(list)

    // Part 2
    println("Part 2")
    part2(inputTest)
    part2(input)
}

fun part1(list: List<SnailfishNumber>) {
    val reduce = list.reduce { acc, snailfishNumber -> acc + snailfishNumber }
    println("Magnitude of the sum is: ${reduce.magnitude()}")
}

fun part2(list: List<String>) {
    var maxMagnitude = 0
    for (i in list.indices) {
        for (j in list.indices) {
            if (i != j) {
                val snailfishNumber1 = SnailfishNumber.parse(list[i])
                val snailfishNumber2 = SnailfishNumber.parse(list[j])
                maxMagnitude = maxOf(maxMagnitude, (snailfishNumber1 + snailfishNumber2).magnitude())
            }
        }
    }

    println("Max magnitude of two snailfish numbers is $maxMagnitude")
}

class SnailfishNumber(
    var left: SnailfishNumber? = null,
    var right: SnailfishNumber? = null,
    var parent: SnailfishNumber? = null,
    var value: Int? = null
) {
    init {
        left?.parent = this
        right?.parent = this
    }

    companion object Factory {
        fun parse(str: String): SnailfishNumber {
            // Find pairs of digits
            val regex = "\\[(pair\\d+|\\d),(pair\\d+|\\d)]".toRegex()
            val map = mutableMapOf<String, SnailfishNumber>()
            var mapId = 0
            var currentNumber: SnailfishNumber
            var line = str
            do {
                mapId++
                val matchResult: MatchResult = regex.find(line)!!
                val leftMatch = matchResult.groupValues[1]
                val rightMatch = matchResult.groupValues[2]

                val left = map.getOrElse(leftMatch) { SnailfishNumber(value = leftMatch.toInt()) }
                val right = map.getOrElse(rightMatch) { SnailfishNumber(value = rightMatch.toInt()) }

                currentNumber = SnailfishNumber(left, right)

                map["pair$mapId"] = currentNumber
                line = line.replaceFirst(regex, "pair$mapId")

            } while (line.contains('['))
            return currentNumber
        }
    }

    /**
     * Find all SnailfishNumber children which have final values
     */
    private fun getFinalValues(): MutableList<SnailfishNumber> {
        return if (this.value != null) mutableListOf(this)
        else {
            val list = left!!.getFinalValues()
            list.addAll(right!!.getFinalValues())
            list
        }
    }

    /**
     * While we found a child that can explode, we explode it
     * Then, while we found a child that can split, we split it
     * Then, the reduction is done
     */
    private fun reduce() {
        while (true) {
            val finalValues = getFinalValues()
            val snailfishNumberThatCanExplode = finalValues.firstOrNull { it.canExplode() }
            if (snailfishNumberThatCanExplode != null) {
                snailfishNumberThatCanExplode.parent!!.explode()
                continue
            }
            val snailfishNumberThatCanSplit = finalValues.firstOrNull { it.canSplit() }
            if (snailfishNumberThatCanSplit != null) {
                snailfishNumberThatCanSplit.split()
                continue
            }
            break
        }
    }

    /**
     * If any pair is nested inside four pairs
     */
    private fun canExplode(): Boolean {
        return this.parent?.parent?.parent?.parent?.parent != null
    }

    /**
     * To explode a pair:
     *  - the pair's left value is added to the first regular number to the left of the exploding pair (if any),
     *  - and the pair's right value is added to the first regular number to the right of the exploding pair (if any).
     * Exploding pairs will always consist of two regular numbers.
     * Then, the entire exploding pair is replaced with the regular number 0.
     */
    private fun explode() {
        // We seek for the right child of the left number
        val leftToAdd = getLeftNumber()?.getRightMostChild()
        // We seek for the left child of the right number
        val rightToAdd = getRightNumber()?.getLeftMostChild()

        // We add the right / left values to the neighbours if they exist
        leftToAdd?.value = leftToAdd!!.value!! + left!!.value!!
        rightToAdd?.value = rightToAdd!!.value!! + right!!.value!!

        // We reset the left and right children and set the value to 0
        left = null
        right = null
        value = 0
    }

    /**
     * If any regular number is 10 or greater
     */
    private fun canSplit(): Boolean {
        return this.value != null && this.value!! >= 10
    }

    /**
     * To split a regular number, replace it with a pair
     * The left element of the pair should be the regular number divided by two and rounded down,
     * while the right element of the pair should be the regular number divided by two and rounded up
     */
    private fun split() {
        val leftValue = floor(this.value!! / 2.0).toInt()
        val rightValue = ceil(this.value!! / 2.0).toInt()
        this.left = SnailfishNumber(value = leftValue, parent = this)
        this.right = SnailfishNumber(value = rightValue, parent = this)
        this.value = null
    }

    private fun getLeftNumber(): SnailfishNumber? {
        return if (parent == null || parent!!.left == null) null
        else if (parent!!.left != this) parent!!.left
        else parent!!.getLeftNumber()
    }

    private fun getRightNumber(): SnailfishNumber? {
        return if (parent == null || parent!!.right == null) null
        else if (parent!!.right != this) parent!!.right
        else parent!!.getRightNumber()
    }

    private fun getLeftMostChild(): SnailfishNumber {
        return if (this.value != null) this
        else if (this.left!!.value != null) left!!
        else left!!.getLeftMostChild()
    }

    private fun getRightMostChild(): SnailfishNumber {
        return if (this.value != null) this
        else if (this.right!!.value != null) right!!
        else right!!.getRightMostChild()
    }

    fun magnitude(): Int {
        return if (value != null) value!!
        else {
            3 * left!!.magnitude() + 2 * right!!.magnitude()
        }
    }

    operator fun plus(other: SnailfishNumber): SnailfishNumber {
        val new = SnailfishNumber(left = this, right = other)
        new.reduce()
        return new
    }
}