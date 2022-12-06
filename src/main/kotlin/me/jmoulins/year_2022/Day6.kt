package me.jmoulins.year_2022

import java.io.File

fun main() {
    var input = File("src/main/resources/year_2022/day6/input.txt").readLines().first()
    var markerFound = false
    var position = 3
    while (!markerFound) {
        position++
        val chars = input.take(4).toList()
        markerFound = chars.count { it == chars[0] } == 1
                && chars.count { it == chars[1] } == 1
                && chars.count { it == chars[2] } == 1
                && chars.count { it == chars[3] } == 1
        input = input.drop(1)
    }
    println("Part 1 - Characters needed to be processed before the first start-of-packet marker is detected : $position")

    input = File("src/main/resources/year_2022/day6/input.txt").readLines().first()
    markerFound = false
    position = 13
    while (!markerFound) {
        position++
        val chars = input.take(14).toList()
        markerFound = chars.count { it == chars[0] } == 1
                && chars.count { it == chars[1] } == 1
                && chars.count { it == chars[2] } == 1
                && chars.count { it == chars[3] } == 1
                && chars.count { it == chars[4] } == 1
                && chars.count { it == chars[5] } == 1
                && chars.count { it == chars[6] } == 1
                && chars.count { it == chars[7] } == 1
                && chars.count { it == chars[8] } == 1
                && chars.count { it == chars[9] } == 1
                && chars.count { it == chars[10] } == 1
                && chars.count { it == chars[11] } == 1
                && chars.count { it == chars[12] } == 1
                && chars.count { it == chars[13] } == 1
        input = input.drop(1)
    }
    println("Part 2 - Characters needed to be processed before the first start-of-packet marker is detected : $position")
}