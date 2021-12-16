package me.jmoulins.year_2021

import me.jmoulins.utils.asBinStr
import me.jmoulins.utils.toBigInteger
import me.jmoulins.utils.toBooleanArray
import me.jmoulins.utils.toInt
import java.io.File
import java.math.BigInteger
import java.util.*

fun main() {
    val input = readDay16File("src/main/resources/year_2021/day16/input.txt")
    val booleanArray = convertHexToBooleanArray(input)
    val packet = Packet.build(booleanArray)

    // Part 1
    println("Part 1")
    println("Sum of all versions for input is ${packet.sumAllVersionNumbers()}")

    // Part 2
    println("Part 2")
    println("Expression gives the number ${packet.evaluateExpression()}")
}

fun readDay16File(path: String): String {
    return File(path).readLines().first()
}

fun convertHexToBooleanArray(hexStr: String): BooleanArray {
    return HexFormat.of().parseHex(hexStr).toBooleanArray()
}

class Packet(
    private var version: Int = 0,
    private var typeId: Int = 0,
    var length: Int = 0
) {
    private var subPackets: MutableList<Packet> = mutableListOf()
    private var value: BigInteger = BigInteger.ZERO

    constructor(version: Int, typeId: Int, length: Int, value: BigInteger) : this(version, typeId, length) {
        this.value = value
    }

    constructor(version: Int, typeId: Int, length: Int, subPackets: MutableList<Packet>) : this(
        version,
        typeId,
        length
    ) {
        this.subPackets = subPackets
    }

    fun sumAllVersionNumbers(): Int {
        var version = this.version
        version += this.subPackets.sumOf { it.sumAllVersionNumbers() }
        return version
    }

    fun evaluateExpression(): BigInteger {
        return when (this.typeId) {
            0 -> this.subPackets.sumOf { it.evaluateExpression() }
            1 -> this.subPackets.map { it.evaluateExpression() }
                .reduce { acc, value -> acc * value }
            2 -> this.subPackets.minOf { it.evaluateExpression() }
            3 -> this.subPackets.maxOf { it.evaluateExpression() }
            4 -> this.value
            5 -> {
                (this.subPackets.first().evaluateExpression() > this.subPackets.last()
                    .evaluateExpression()).toBigInteger()
            }
            6 -> {
                (this.subPackets.first().evaluateExpression() < this.subPackets.last()
                    .evaluateExpression()).toBigInteger()
            }
            7 -> {
                (this.subPackets.first().evaluateExpression() == this.subPackets.last()
                    .evaluateExpression()).toBigInteger()
            }
            else -> throw IllegalArgumentException()
        }
    }

    companion object Factory {
        fun build(booleanArray: BooleanArray): Packet {
            val version = Integer.parseInt(booleanArray.take(3).toBooleanArray().asBinStr, 2)
            var remainingBits = booleanArray.drop(3)
            val typeId = Integer.parseInt(remainingBits.take(3).toBooleanArray().asBinStr, 2)
            var length = 0
            remainingBits = booleanArray.drop(6)

            if (typeId == 4) {
                length += 6
                var binStr = ""
                do {
                    val isLastGroup = !remainingBits.first()
                    remainingBits = remainingBits.drop(1)
                    binStr += remainingBits.take(4).toBooleanArray().asBinStr
                    remainingBits = remainingBits.drop(4)
                    length += 5
                } while (!isLastGroup)
                val value = BigInteger(binStr, 2)
                return Packet(version, typeId, length, value)
            } else {
                val lengthTypeID = remainingBits.first().toInt()
                val nbOfBitsForLength = if (remainingBits.first()) 11 else 15
                remainingBits = remainingBits.drop(1)
                val lengthOfSubPackets =
                    Integer.parseInt(remainingBits.take(nbOfBitsForLength).toBooleanArray().asBinStr, 2)
                remainingBits = remainingBits.drop(nbOfBitsForLength)

                // length in bits
                val subPackets: MutableList<Packet> = mutableListOf()
                if (lengthTypeID == 0) {
                    var subPacketBits = remainingBits.take(lengthOfSubPackets).toBooleanArray()
                    while (subPacketBits.isNotEmpty() && subPacketBits.reduce { acc, b -> acc || b }) {
                        val packet = build(subPacketBits)
                        subPacketBits = subPacketBits.drop(packet.length).toBooleanArray()
                        subPackets.add(packet)
                    }
                }
                // length is nb of subPackets
                else {
                    for (i in 1..lengthOfSubPackets) {
                        val packet = build(remainingBits.toBooleanArray())
                        remainingBits = remainingBits.drop(packet.length)
                        subPackets.add(packet)
                    }
                }
                length =
                    booleanArray.take(6 + 1 + nbOfBitsForLength + subPackets.sumOf { it.length }).toBooleanArray().size
                return Packet(version, typeId, length, subPackets)
            }
        }
    }
}