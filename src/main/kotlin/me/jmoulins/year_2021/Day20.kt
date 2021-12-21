package me.jmoulins.year_2021

import me.jmoulins.utils.toInt
import java.io.File

fun main() {
    val fileTest = readDay20File("src/main/resources/year_2021/day20/input-test.txt")
    val file = readDay20File("src/main/resources/year_2021/day20/input.txt")

    // Part 1
    enhance(fileTest.first, fileTest.second, 2)
    enhance(file.first, file.second, 2)

    // Part 2
    enhance(fileTest.first, fileTest.second, 50)
    enhance(file.first, file.second, 50)
}

fun readDay20File(path: String): Pair<List<List<Int>>, String> {
    val lines = File(path).readLines()
    val imageEnhancementAlgorithm = lines[0].map { (it == '#').toInt() }.joinToString("")
    val input = lines.drop(2).map { str -> str.map { (it == '#').toInt() } }
    return input to imageEnhancementAlgorithm
}

fun enhance(input: List<List<Int>>, imageEnhancementAlgorithm: String, steps: Int) {
    println("Input before enhancement")
    print(input)
    var image = input.toList()
    for (step in 1..steps) {
        val infinitePixel = if (imageEnhancementAlgorithm.first() == '1' && step % 2 == 0) "1" else "0"
        val output: List<MutableList<Int>> =
            List(image.size + 2) { MutableList(image.first().size + 2) { infinitePixel.toInt() } }
        for (j in -1..image.size) {
            for (i in -1..image.first().size) {
                var binStr = ""
                for (y in -1..1) {
                    for (x in -1..1) {
                        binStr += image.getOrNull(j + y)?.getOrNull(i + x) ?: infinitePixel
                    }
                }
                val index = binStr.toInt(2)
                val c = imageEnhancementAlgorithm[index]
                output[j + 1][i + 1] = (c == '1').toInt()
            }
        }
        image = output
    }
    println("Input after enhancement ($steps steps)")
    print(image)
    println("Nb of pixels lit after $steps steps: ${image.sumOf { it.count { i -> i == 1 } }}")
}

fun print(image: List<List<Int>>) {
    for (y in image.indices) {
        for (x in image.first().indices) {
            print(if (image[y][x] == 1) "#" else ".")
        }
        println()
    }
    println()
}