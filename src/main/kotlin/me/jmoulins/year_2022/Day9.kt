package me.jmoulins.year_2022

import java.awt.Point
import java.io.File
import kotlin.math.sign

fun main() {
    val input: List<Pair<String, Int>> = File("src/main/resources/year_2022/day9/input.txt").readLines()
        .map {
            val s = it.split(" ")
            s.first() to s.last().toInt()
        }

    println("Part 1 - Number of visited positions by the tail: ${part1(input)}")
    println("Part 2 - Number of visited positions by the tail: ${part2(input)}")
}

fun part1(input: List<Pair<String, Int>>): Int {
    val head = Point(0, 0)
    val tail = Point(0, 0)
    val tailVisitedPositions = mutableListOf<Point>()
    for ((direction, steps) in input) {
        for (i in 1..steps) {
            when (direction) {
                "U" -> {
                    head.translate(0, 1)
                    if (head.distanceSq(tail) > 2) {
                        tail.translate(sign(head.getX() - tail.getX()).toInt(), sign(head.getY() - tail.getY()).toInt())
                    }
                }
                "D" -> {
                    head.translate(0, -1)
                    if (head.distanceSq(tail) > 2) {
                        tail.translate(sign(head.getX() - tail.getX()).toInt(), sign(head.getY() - tail.getY()).toInt())
                    }
                }
                "R" -> {
                    head.translate(1, 0)
                    if (head.distanceSq(tail) > 2) {
                        tail.translate(sign(head.getX() - tail.getX()).toInt(), sign(head.getY() - tail.getY()).toInt())
                    }
                }
                "L" -> {
                    head.translate(-1, 0)
                    if (head.distanceSq(tail) > 2) {
                        tail.translate(sign(head.getX() - tail.getX()).toInt(), sign(head.getY() - tail.getY()).toInt())
                    }
                }
            }
            tailVisitedPositions.add(Point(tail))
        }
    }

    return tailVisitedPositions.distinct().count()
}

fun part2(input: List<Pair<String, Int>>): Int {
    val points = List(10) { Point(0, 0) }
    val tailVisitedPositions = mutableListOf<Point>()
    for ((direction, steps) in input) {
        for (i in 1..steps) {
            when (direction) {
                "U" -> {
                    points.first().translate(0, 1)
                    for (j in 1..9) {
                        if (points[j].distanceSq(points[j-1]) > 2) {
                            points[j].translate(
                                sign(points[j-1].getX() - points[j].getX()).toInt(),
                                sign(points[j-1].getY() - points[j].getY()).toInt()
                            )
                        }
                    }
                }
                "D" -> {
                    points.first().translate(0, -1)
                    for (j in 1..9) {
                        if (points[j].distanceSq(points[j-1]) > 2) {
                            points[j].translate(
                                sign(points[j-1].getX() - points[j].getX()).toInt(),
                                sign(points[j-1].getY() - points[j].getY()).toInt()
                            )
                        }
                    }
                }
                "R" -> {
                    points.first().translate(1, 0)
                    for (j in 1..9) {
                        if (points[j].distanceSq(points[j-1]) > 2) {
                            points[j].translate(
                                sign(points[j-1].getX() - points[j].getX()).toInt(),
                                sign(points[j-1].getY() - points[j].getY()).toInt()
                            )
                        }
                    }
                }
                "L" -> {
                    points.first().translate(-1, 0)
                    for (j in 1..9) {
                        if (points[j].distanceSq(points[j-1]) > 2) {
                            points[j].translate(
                                sign(points[j-1].getX() - points[j].getX()).toInt(),
                                sign(points[j-1].getY() - points[j].getY()).toInt()
                            )
                        }
                    }
                }
            }
            tailVisitedPositions.add(Point(points.last()))
        }
    }

    return tailVisitedPositions.distinct().count()
}