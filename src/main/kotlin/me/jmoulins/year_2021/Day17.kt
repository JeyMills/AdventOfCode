package me.jmoulins.year_2021

import kotlin.math.max

fun main() {
    val xTargetMinTest = 20
    val xTargetMaxTest = 30
    val yTargetMinTest = -10
    val yTargetMaxTest = -5

    val xTargetMin = 209
    val xTargetMax = 238
    val yTargetMin = -86
    val yTargetMax = -59

    println("yMax is ${part1(yTargetMinTest)}")
    println("yMax is ${part1(yTargetMin)}")
    println(
        "${
            part2(
                xTargetMinTest,
                xTargetMaxTest,
                yTargetMinTest,
                yTargetMaxTest
            )
        } initial velocity values cause the probe to be within the target area"
    )
    println(
        "${
            part2(
                xTargetMin,
                xTargetMax,
                yTargetMin,
                yTargetMax
            )
        } initial velocity values cause the probe to be within the target area"
    )
}

fun part1(yTargetMin: Int): Int {
    return (-yTargetMin * ((-yTargetMin.toFloat() - 1) / 2)).toInt()
}

fun part2(xTargetMin: Int, xTargetMax: Int, yTargetMin: Int, yTargetMax: Int): Int {
    var count = 0
    for (velX0 in 0..xTargetMax) {
        for (velY0 in yTargetMin..-yTargetMin) {
            if (inTarget(velX0, velY0, xTargetMin, xTargetMax, yTargetMin, yTargetMax)) count += 1
        }
    }
    return count
}

fun inTarget(velX0: Int, velX1: Int, xTargetMin: Int, xTargetMax: Int, yTargetMin: Int, yTargetMax: Int): Boolean {
    var x = 0
    var y = 0
    var velX = velX0
    var velY = velX1
    while (x <= xTargetMax && y >= yTargetMin) {
        x += velX
        y += velY
        if (x in xTargetMin..xTargetMax && y in yTargetMin..yTargetMax ) {
            return true
        }
        velX = max(0, velX - 1)
        velY -= 1
    }
    return false
}