package me.jmoulins.year_2021

import java.io.File
import kotlin.math.absoluteValue
import kotlin.math.pow
import kotlin.math.sqrt

fun main() {
    val scannersTest = readDay19File("src/main/resources/year_2021/day19/input-test.txt")
    val scanners = readDay19File("src/main/resources/year_2021/day19/input.txt")

    val pointsTest = resolve(scannersTest)
    val nbBeaconsTest = pointsTest.first.count()
    val maxDistanceTest = buildManhattanDistances(pointsTest.second).maxByOrNull { it.value }
    println("Nb beacons for input test: $nbBeaconsTest")
    println("Max distance between two scanners for input test: $maxDistanceTest")
    println()

    val points = resolve(scanners)
    val nbBeacons = points.first.count()
    val maxDistance = buildManhattanDistances(points.second).maxByOrNull { it.value }
    println("Nb beacons for input: $nbBeacons")
    println("Max distance between two scanners for input: $maxDistance")

}

fun resolve(scanners: List<List<Point>>): Pair<List<Point>, List<Point>> {
    val firstScanner = scanners.first()
    var pointsRelativeToFirstScanner = firstScanner.toMutableList()
    val unresolvedScanners: MutableList<List<Point>> = scanners.drop(1).map { it }.toMutableList()
    val scannersPoint = mutableListOf(Point(0, 0, 0))

    // While there are coordinate not relative to scanner 0
    while (unresolvedScanners.isNotEmpty()) {
        val resolvedScanners: MutableList<List<Point>> = mutableListOf()

        // For each scanner
        for (i in unresolvedScanners) {
            // We found the distances between points for coordinates relative to scanner 0
            val distancesBetweenPointsRelativesToFirstScanner = buildEuclideanDistances(pointsRelativeToFirstScanner)
            // We found the distances between points for coordinates relative to the current scanner
            val currentScannerDistances = buildEuclideanDistances(i)

            // We seek for equal distances between the two maps 
            val equalDistances: Map<Pair<Point, Point>, Double> =
                distancesBetweenPointsRelativesToFirstScanner.filter { currentScannerDistances.values.contains(it.value) }

            // We reduce to have the distinct points ordered by the most represented point
            val distinctPointsWithEqualDistance = equalDistances.keys
                .flatMap { it.toList() }
                .groupingBy { it }
                .eachCount()
                .let { count -> equalDistances.keys.flatMap { it.toList() }.sortedByDescending { count[it] } }
                .distinct()
            val countSameBeacons: Int = distinctPointsWithEqualDistance.count()

            // If there are at least 12 matching beacons, we transform the coordinates relative to the current scanner
            // to coordinates relative to scanner 0
            if (countSameBeacons >= 12) {
                // We take 3 points (difference between point must have different absolute coordinates)
                var threePoints: List<Point> = distinctPointsWithEqualDistance.take(3)
                var diff1 = threePoints[0] - threePoints[1]
                var diff2 = threePoints[0] - threePoints[2]
                var diff3 = threePoints[1] - threePoints[2]
                while (diff1.hasCommonCoordinates() || diff2.hasCommonCoordinates() || diff3.hasCommonCoordinates()) {
                    threePoints = distinctPointsWithEqualDistance.drop(1).take(3)
                    diff1 = threePoints[0] - threePoints[1]
                    diff2 = threePoints[0] - threePoints[2]
                    diff3 = threePoints[1] - threePoints[2]
                }

                // We deduce create two pairs of matching points :
                // pointA1 is relative to first scanner and matches with pointA2 which is relative to current scanner 
                // pointB1 is relative to first scanner and matches with pointB2 which is relative to current scanner 
                val twoPtsDistRelToFirstScanner =
                    buildEuclideanDistances(threePoints).entries.take(2).associateBy({ it.value }) { it.key }
                        .toSortedMap()
                val twoPtsDistRelToCurrentScanner =
                    currentScannerDistances.filter { twoPtsDistRelToFirstScanner.containsKey(it.value) }.entries.associateBy(
                        { it.value }) { it.key }
                        .toSortedMap()
                val pointA1 =
                    twoPtsDistRelToFirstScanner.values.flatMap { pair -> pair.toList() }.groupingBy { it }.eachCount()
                        .maxByOrNull { it.value }!!.key
                val pointA2 =
                    twoPtsDistRelToCurrentScanner.values.flatMap { pair -> pair.toList() }.groupingBy { it }.eachCount()
                        .maxByOrNull { it.value }!!.key
                val pointB1 =
                    if (twoPtsDistRelToFirstScanner.entries.first().value.first == pointA1) twoPtsDistRelToFirstScanner.entries.first().value.second else twoPtsDistRelToFirstScanner.entries.first().value.first
                val pointB2 =
                    if (twoPtsDistRelToCurrentScanner.entries.first().value.first == pointA2) twoPtsDistRelToCurrentScanner.entries.first().value.second else twoPtsDistRelToCurrentScanner.entries.first().value.first

                // We seek transformation matrix and transform coordinates relative to current scanner to coordinates relative to first scanner
                val transformation = determineTransformation((pointA1 to pointA2), (pointB1 to pointB2))
                val transformed = transformCoordinatesWithVector(i, transformation)

                // We seek the coordinates of the current scanner relative to the first scanner
                val scannerCoordinates = pointA1 - transformPointWithVector(pointA2, transformation)
                println("Scanner found at $scannerCoordinates")
                scannersPoint.add(scannerCoordinates)

                // We translate the coordinates of all points relative to this scanner
                val translated = transformed.map { it + scannerCoordinates }

                // We have these points to the list of coordinates relative to the first scanner and remove duplicates
                pointsRelativeToFirstScanner.addAll(translated)
                pointsRelativeToFirstScanner = pointsRelativeToFirstScanner.distinct().toMutableList()

                // We add this list of coordinates to the resolved list
                resolvedScanners.add(i)
            }
        }
        // We remove the resolved list from the unresolved list
        unresolvedScanners.removeAll(resolvedScanners)
    }

    // We return a pair containing the list of all points relative to the first scanner and coordinates of all the scanners relative to the first scanner
    return pointsRelativeToFirstScanner to scannersPoint
}

fun readDay19File(path: String): List<List<Point>> {
    val scanners = mutableListOf<List<Point>>()
    var points = mutableListOf<Point>()
    File(path).readLines().forEach {
        run {
            if (it.isNotEmpty()) {
                if (it.contains("scanner")) {
                    points = mutableListOf()
                    scanners.add(points)
                } else {
                    val coords = it.split(",")
                    points.add(Point(coords.first().toInt(), coords[1].toInt(), coords.last().toInt()))
                }
            }
        }
    }
    return scanners
}

fun determineTransformation(match1: Pair<Point, Point>, match2: Pair<Point, Point>): List<List<Int>> {
    val diff1 = match1.first - match2.first
    val diff2 = match1.second - match2.second

    val transformation: List<MutableList<Int>> = List(3) { mutableListOf(0, 0, 0) }
    for (i in 0 until 3) {
        val coords = when (i) {
            0 -> diff1.x
            1 -> diff1.y
            2 -> diff1.z
            else -> error("Wrong argument")
        }

        when (coords) {
            diff2.x -> transformation[i][0] = 1
            -diff2.x -> transformation[i][0] = -1
            diff2.y -> transformation[i][1] = 1
            -diff2.y -> transformation[i][1] = -1
            diff2.z -> transformation[i][2] = 1
            -diff2.z -> transformation[i][2] = -1
            else -> error("Wrong argument")
        }
    }
    return transformation
}

fun buildEuclideanDistances(coordinates: List<Point>): Map<Pair<Point, Point>, Double> {
    val distances: MutableMap<Pair<Point, Point>, Double> = mutableMapOf()
    for (i in coordinates.indices) {
        val firstCoords = coordinates[i]
        for (j in i + 1 until coordinates.size) {
            val secondCoords = coordinates[j]
            distances[Pair(firstCoords, secondCoords)] = firstCoords.euclideanDistance(secondCoords)
        }
    }
    val filter = distances.values.groupingBy { it }.eachCount().filter { it.value > 1 }
    if (filter.isNotEmpty()) {
        return distances.filter { it.value !in filter }
    }
    return distances
}

fun buildManhattanDistances(coordinates: List<Point>): Map<Pair<Point, Point>, Int> {
    val distances: MutableMap<Pair<Point, Point>, Int> = mutableMapOf()
    for (i in coordinates.indices) {
        val firstCoords = coordinates[i]
        for (j in i + 1 until coordinates.size) {
            val secondCoords = coordinates[j]
            distances[Pair(firstCoords, secondCoords)] = firstCoords.manhattanDistance(secondCoords)
        }
    }
    return distances
}

fun transformCoordinatesWithVector(coordinates: List<Point>, transformation: List<List<Int>>): List<Point> {
    val newCoordinates: MutableList<Point> = mutableListOf()
    coordinates.forEach {
        run {
            newCoordinates.add(transformPointWithVector(it, transformation))
        }
    }
    return newCoordinates
}

fun transformPointWithVector(point: Point, transformation: List<List<Int>>): Point {
    val x = transformation[0][0] * point.x + transformation[0][1] * point.y + transformation[0][2] * point.z
    val y = transformation[1][0] * point.x + transformation[1][1] * point.y + transformation[1][2] * point.z
    val z = transformation[2][0] * point.x + transformation[2][1] * point.y + transformation[2][2] * point.z
    return Point(x, y, z)
}

data class Point(val x: Int, val y: Int, val z: Int) {

    operator fun plus(other: Point): Point =
        Point(x + other.x, y + other.y, z + other.z)

    operator fun minus(other: Point): Point =
        Point(x - other.x, y - other.y, z - other.z)

    fun hasCommonCoordinates(): Boolean {
        return x.absoluteValue == y.absoluteValue || x.absoluteValue == z.absoluteValue || y.absoluteValue == z.absoluteValue
    }

    fun manhattanDistance(other: Point): Int {
        return (this.x - other.x).absoluteValue + (this.y - other.y).absoluteValue + (this.z - other.z).absoluteValue
    }

    fun euclideanDistance(other: Point): Double {
        return sqrt((x - other.x).toDouble().pow(2) + (y - other.y).toDouble().pow(2) + (z - other.z).toDouble().pow(2))
    }
}