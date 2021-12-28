package me.jmoulins.year_2021

import java.io.File
import java.util.*
import kotlin.math.abs

fun main() {
    // Part 1
    val inputTest = part1("src/main/resources/year_2021/day23/input-test.txt")
    println(searchLowerCost(inputTest))
    val input = part1("src/main/resources/year_2021/day23/input.txt")
    println(searchLowerCost(input))

    // Part 2
    val inputTest2 = part2("src/main/resources/year_2021/day23/input-test.txt")
    println(searchLowerCost(inputTest2))
    val input2 = part2("src/main/resources/year_2021/day23/input.txt")
    println(searchLowerCost(input2))
}

fun searchLowerCost(amphipodState: AmphipodState): Int {
    val priorityQueue: PriorityQueue<AmphipodStateWithCost> =
        PriorityQueue<AmphipodStateWithCost>().apply { add(AmphipodStateWithCost(amphipodState, 0)) }
    val states = mutableSetOf<AmphipodStateWithCost>()
    val currentCosts = mutableMapOf<AmphipodState, Int>().withDefault { Int.MAX_VALUE }

    while (priorityQueue.isNotEmpty()) {
        val current = priorityQueue.poll().also { states.add(it) }
        current.amphipodState.nextPossibleStates().forEach { next ->
            if (!states.contains(next)) {
                val newCost = current.cost + next.cost
                if (newCost < currentCosts.getValue(next.amphipodState)) {
                    currentCosts[next.amphipodState] = newCost
                    priorityQueue.add(AmphipodStateWithCost(next.amphipodState, newCost))
                }
            }
        }
    }
    return currentCosts.getValue(currentCosts.keys.first { it.isOrganized() })
}

fun part1(path: String): AmphipodState {
    return AmphipodState.parse(File(path).readLines())
}

fun part2(path: String): AmphipodState {
    var lines = File(path).readLines()
    lines = lines.take(3) + "  #D#C#B#A#  " + "  #D#B#A#C#  " + lines.takeLast(2)
    return AmphipodState.parse(lines)
}

data class AmphipodState(private val positions: List<List<Char>>) {
    private val hallway = positions[0]
    private val rooms = positions.drop(1)
    private val destinationRooms = mapOf(
        'A' to Room('A', 2, rooms.map { row -> row[2] }),
        'B' to Room('B', 4, rooms.map { row -> row[4] }),
        'C' to Room('C', 6, rooms.map { row -> row[6] }),
        'D' to Room('D', 8, rooms.map { row -> row[8] })
    )
    private val costs = mapOf(
        'A' to 1,
        'B' to 10,
        'C' to 100,
        'D' to 1000,
    )
    private val hallwayStopIndexes
        get() = listOf(0, 1, 3, 5, 7, 9, 10).filter { hallway[it] == '.' }

    fun isOrganized(): Boolean {
        return destinationRooms.values.all { it.hasOnlyValidAmphipods() }
    }

    fun nextPossibleStates(): List<AmphipodStateWithCost> {
        return buildList {
            movableAmphipodInHallway().forEach {
                val room = destinationRooms.getValue(it.value)
                if (hallwayPathIsClear(it.index, room.index)) {
                    val positionInRoom = room.content.lastIndexOf('.') + 1
                    val cost = (abs(it.index - room.index) + positionInRoom) * costs.getValue(it.value)
                    add(AmphipodStateWithCost(AmphipodState(positions.map { row -> row.toMutableList() }.apply {
                        get(0)[it.index] = '.'
                        get(positionInRoom)[room.index] = it.value
                    }), cost))
                }
            }

            amphipodInWrongRoom().forEach { room ->
                val amphipod = room.content.withIndex().first { it.value != '.' }
                hallwayStopIndexes.forEach { index ->
                    if (hallwayPathIsClear(index, room.index)) {
                        val cost = (abs(index - room.index) + amphipod.index + 1) * costs.getValue(amphipod.value)
                        add(AmphipodStateWithCost(AmphipodState(positions.map { row -> row.toMutableList() }.apply {
                            get(amphipod.index + 1)[room.index] = '.'
                            get(0)[index] = amphipod.value
                        }), cost))
                    }
                }
            }
        }
    }

    private fun movableAmphipodInHallway() = hallway.withIndex()
        .filter { it.value.isLetter() && destinationRooms.getValue(it.value).isEmptyOrHasAllValidAmphipods() }

    private fun amphipodInWrongRoom() = destinationRooms.values.filter { it.hasAmphipodsWithWrongType() }

    private fun hallwayPathIsClear(start: Int, end: Int) = hallway.slice(
        when (start > end) {
            true -> (start - 1) downTo end
            false -> (start + 1)..end
        }
    ).all { it == '.' }

    companion object {
        fun parse(input: List<String>) =
            AmphipodState(input.drop(1).dropLast(1).map { it.drop(1).dropLast(1).toList() })
    }
}

private class Room(val char: Char, val index: Int, val content: List<Char>) {
    fun hasOnlyValidAmphipods() = content.all { it == char }

    fun isEmptyOrHasAllValidAmphipods() = content.all { it == '.' || it == char }

    fun hasAmphipodsWithWrongType() = !isEmptyOrHasAllValidAmphipods()
}

class AmphipodStateWithCost(val amphipodState: AmphipodState, val cost: Int) : Comparable<AmphipodStateWithCost> {
    override fun compareTo(other: AmphipodStateWithCost) = cost.compareTo(other.cost)
}