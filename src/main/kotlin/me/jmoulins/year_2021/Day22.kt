package me.jmoulins.year_2021

import java.io.File
import kotlin.math.max
import kotlin.math.min

fun main() {
    // Part 1
    println("Part 1")
    part1(readInstructions("src/main/resources/year_2021/day22/input-test0.txt"))
    println()
    part1(readInstructions("src/main/resources/year_2021/day22/input-test1.txt"))
    println()
    part1(readInstructions("src/main/resources/year_2021/day22/input.txt"))
    println()

    // Part 2
    println("Part 2")
    part2(readInstructions("src/main/resources/year_2021/day22/input-test2.txt"))
    println()
    part2(readInstructions("src/main/resources/year_2021/day22/input.txt"))
}

fun part1(steps: List<Step>) {
    run(steps.filter { it.xRange.first >= -50 && it.xRange.last <= 50 && it.yRange.first >= -50 && it.yRange.last <= 50 && it.zRange.first >= -50 && it.zRange.last <= 50 })
}

fun part2(steps: List<Step>) {
    run(steps)
}

fun run(steps: List<Step>) {
    val cuboidsOn = mutableListOf<Cuboid>()
    steps.forEach { step ->
        val stepCuboid = Cuboid(step.xRange, step.yRange, step.zRange)
        val cuboidsOnImpacted = cuboidsOn.filter { it.hasIntersectionWith(stepCuboid) }
        if (step.on) {
            cuboidsOn.add(stepCuboid)
        }
        cuboidsOnImpacted.forEach {
            cuboidsOn.addAll(it.subtract(stepCuboid))
        }
        cuboidsOn.removeAll(cuboidsOnImpacted)
    }
    val countCubesOn = cuboidsOn.sumOf { it.countCubes() }
    println("There are $countCubesOn cubes on")
}

fun readInstructions(path: String): List<Step> {
    return File(path).readLines().map {
        val state = it.split(" ").first() == "on"
        val xRange = LongRange(
            it.split(" ").last().split(",").first().drop(2).split("..").first().toLong(),
            it.split(" ").last().split(",").first().drop(2).split("..").last().toLong()
        )
        val yRange = LongRange(
            it.split(" ").last().split(",")[1].drop(2).split("..").first().toLong(),
            it.split(" ").last().split(",")[1].drop(2).split("..").last().toLong()
        )
        val zRange = LongRange(
            it.split(" ").last().split(",").last().drop(2).split("..").first().toLong(),
            it.split(" ").last().split(",").last().drop(2).split("..").last().toLong()
        )
        Step(xRange, yRange, zRange, state)
    }
}

data class Step(val xRange: LongRange, val yRange: LongRange, val zRange: LongRange, val on: Boolean)

data class Cuboid(val xRange: LongRange, val yRange: LongRange, val zRange: LongRange) {

    fun hasIntersectionWith(other: Cuboid): Boolean {
        return max(this.xRange.first, other.xRange.first) <= min(this.xRange.last, other.xRange.last)
                && max(this.yRange.first, other.yRange.first) <= min(this.yRange.last, other.yRange.last)
                && max(this.zRange.first, other.zRange.first) <= min(this.zRange.last, other.zRange.last)
    }

    private fun intersect(other: Cuboid): Cuboid {
        return Cuboid(
            max(this.xRange.first, other.xRange.first)..min(this.xRange.last, other.xRange.last),
            max(this.yRange.first, other.yRange.first)..min(this.yRange.last, other.yRange.last),
            max(this.zRange.first, other.zRange.first)..min(this.zRange.last, other.zRange.last)
        )
    }

    fun subtract(other: Cuboid): List<Cuboid> {
        val intersection = this.intersect(other)
        val cuboids = mutableListOf<Cuboid>()

        // Front cube
        if (this.zRange.last > intersection.zRange.last) cuboids.add(
            Cuboid(
                intersection.xRange,
                intersection.yRange,
                intersection.zRange.last + 1..this.zRange.last
            )
        )
        // Back cube
        if (this.zRange.first < intersection.zRange.first) cuboids.add(
            Cuboid(
                intersection.xRange,
                intersection.yRange,
                this.zRange.first until intersection.zRange.first
            )
        )
        // Upper cube
        if (this.yRange.last > intersection.yRange.last) cuboids.add(
            Cuboid(
                intersection.xRange,
                intersection.yRange.last + 1..this.yRange.last,
                this.zRange
            )
        )
        // Lower cube
        if (this.yRange.first < intersection.yRange.first) cuboids.add(
            Cuboid(
                intersection.xRange,
                this.yRange.first until intersection.yRange.first,
                this.zRange
            )
        )
        // Right cube
        if (this.xRange.last > intersection.xRange.last) cuboids.add(
            Cuboid(
                intersection.xRange.last + 1..this.xRange.last,
                this.yRange,
                this.zRange
            )
        )
        // Left cube
        if (this.xRange.first < intersection.xRange.first) cuboids.add(
            Cuboid(
                this.xRange.first until intersection.xRange.first,
                this.yRange,
                this.zRange
            )
        )
        return cuboids
    }

    fun countCubes(): Long {
        return (xRange.last - xRange.first + 1) * (yRange.last - yRange.first + 1) * (zRange.last - zRange.first + 1)
    }
}