package me.jmoulins.year_2021

import me.jmoulins.utils.toInt
import java.io.File
import kotlin.reflect.KFunction
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberFunctions
import kotlin.reflect.full.memberProperties

fun main() {
    val lines = File("src/main/resources/year_2021/day24/input.txt").readLines()

    val largestModelNumber = findLargestModelNumber(lines)
    ALU.monad(lines, largestModelNumber)
    // result 94992992796199

    val smallestModelNumber = findSmallestModelNumber(lines)
    ALU.monad(lines, smallestModelNumber)
    // result 11931881141161
}

fun findLargestModelNumber(lines: List<String>): Long {
    return findModelNumber(lines, true)
}

fun findSmallestModelNumber(lines: List<String>): Long {
    return findModelNumber(lines, false)
}

fun findModelNumber(lines: List<String>, max: Boolean): Long {
    val y: MutableList<Pair<Int, Int>> = mutableListOf()
    val pairs = lines.chunked(18).map {
        Pair(it[5].substringAfterLast(" ").toInt(), it[15].substringAfterLast(" ").toInt())
    }
    val digits = LongArray(14) { 0L }
    pairs.forEachIndexed { index, pair ->
        if (pair.first > 0) {
            y.add(Pair(index, pair.second))
        } else {
            val lastY = y.removeLast()
            val diff = lastY.second + pair.first
            val digit = (if (max) 9 downTo 1 else 1..9).first { it + diff in 1..9 }
            digits[lastY.first] = digit.toLong()
            digits[index] = (digit + diff).toLong()
        }
    }
    return digits.reduce { acc, i -> acc * 10 + i }
}

data class ALU(var x: Long = 0, var y: Long = 0, var z: Long = 0, var w: Long = 0) {

    fun inp(a: Long): Long {
        return a
    }

    fun add(a: Long, b: Long): Long {
        return a.plus(b)
    }

    fun mul(a: Long, b: Long): Long {
        return a.times(b)
    }

    fun div(a: Long, b: Long): Long {
        return a.div(b)
    }

    fun mod(a: Long, b: Long): Long {
        return a.mod(b)
    }

    fun eql(a: Long, b: Long): Long {
        return (a == b).toInt().toLong()
    }

    companion object Factory {
        fun monad(lines: List<String>, input: Long) {
            val alu = runALU(lines, input)
            if (alu.z == 0L) {
                println("$input is valid")
            } else {
                println("$input is not valid")
            }
        }

        private fun runALU(lines: List<String>, input: Long): ALU {
            val alu = ALU()
            lines.forEachIndexed { index, s ->
                val lineArguments = s.split(" ")
                val instruction = lineArguments.first()
                val variableToSet = lineArguments[1]
                val a = readInstanceProperty(alu, variableToSet)
                var b = "0"
                if (lineArguments.size > 2) {
                    b = lineArguments[2]
                }
                val aValue = a.getter.call(alu)
                val result = if (instruction != "inp") {
                    val bValue = b.toIntOrNull() ?: readInstanceProperty(alu, b).getter.call(alu)
                    readInstanceFunction(alu, instruction).call(alu, aValue, bValue)
                } else {
                    val int = input.toString()[index / 18].digitToInt()
                    readInstanceFunction(alu, instruction).call(alu, int)
                }
                if (a is KMutableProperty<*>) {
                    a.setter.call(alu, result)
                }
            }
            return alu
        }
    }
}

fun readInstanceProperty(instance: ALU, propertyName: String): KProperty1<ALU, *> {
    return instance::class.memberProperties.first { it.name == propertyName } as KProperty1<ALU, *>
}

fun readInstanceFunction(instance: ALU, functionName: String): KFunction<*> {
    return instance::class.memberFunctions.first { it.name == functionName }
}