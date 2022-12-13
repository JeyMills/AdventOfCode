package me.jmoulins.year_2022

import java.io.File
import java.util.*

fun main() {
    val inputTest = readFile("src/main/resources/year_2022/day12/input-test.txt")
    val input = readFile("src/main/resources/year_2022/day12/input.txt")

    // Part 1
    println("Part 1")

    println("input-test.txt")
    shortestPath("(0,0)", "(5,2)", convertToGraph(inputTest))
    println("input.txt")
    shortestPath("(0,20)", "(52,20)", convertToGraph(input))

    // Part 2
    println("Part 2")

    println("input-test.txt")
    println("Fewest steps (test): ${part2(inputTest, "(5,2)")}")
    println("input.txt")
    println("Fewest steps: ${part2(input, "(52,20)")}")
}

fun shortestPath(start: String, end: String, edges: List<Edge>) {
    with(Graph(edges)) {
        dijkstra(start)
        printPath(end)
        printLowestPath(end)
        println()
    }
}

fun part2(input: Array<CharArray>, end: String): Int {
    val startingPositions = extractStartingPositions(input)
    val edges = convertToGraph(input)
    val possibilities = mutableMapOf<String, Int>()
    for (startingPosition in startingPositions) {
        val lowestPath: Int = with(Graph(edges)) {
            dijkstra(startingPosition)
            getLowestPath(end)
        }
        possibilities[startingPosition] = lowestPath
    }
    return possibilities.values.minOrNull()!!
}

fun readFile(path: String): Array<CharArray> {
    return File(path)
        .readLines()
        .map { s -> s.replace("S", "a").replace("E","z").toCharArray() }
        .toTypedArray()
}

fun extractStartingPositions(input: Array<CharArray>): List<String> {
    val startingPositions: MutableList<String> = mutableListOf()
    for (j in input.indices) {
        for (i in input[j].indices) {
            if (input[j][i] == 'a') startingPositions.add(("($i,$j)"))
        }
    }
    return startingPositions
}

fun convertToGraph(input: Array<CharArray>): List<Edge> {
    val edges: MutableList<Edge> = mutableListOf()
    for (j in input.indices) {
        for (i in input[j].indices) {
            if (i != 0) {
                if (input[j][i] - input[j][i - 1] <= 1) {
                    edges.add(Edge("(${i - 1},$j)", "($i,$j)", 1))
                }
                if (input[j][i - 1] - input[j][i] <= 1) {
                    edges.add(Edge("($i,$j)", "(${i - 1},$j)", 1))
                }
            }
            if (j != 0) {
                if (input[j][i] - input[j - 1][i] <= 1) {
                    edges.add(Edge("($i,${j - 1})", "($i,$j)", 1))
                }
                if (input[j - 1][i] - input[j][i] <= 1) {
                    edges.add(Edge("($i,$j)", "($i,${j - 1})", 1))
                }
            }
        }
    }
    return edges
}

/**
 * Not my implementation after that line
 * @see https://rosettacode.org/wiki/Dijkstra%27s_algorithm#Kotlin
 */
class Edge(val v1: String, val v2: String, val dist: Int)

/** One vertex of the graph, complete with mappings to neighbouring vertices */
class Vertex(private val name: String) : Comparable<Vertex> {

    var dist = Int.MAX_VALUE  // MAX_VALUE assumed to be infinity
    var previous: Vertex? = null
    val neighbours = HashMap<Vertex, Int>()

    fun printPath() {
        when (previous) {
            this -> {
                print(name)
            }
            null -> {
                print("$name(unreached)")
            }
            else -> {
                previous!!.printPath()
                print(" -> $name($dist)")
            }
        }
    }

    override fun compareTo(other: Vertex): Int {
        if (dist == other.dist) return name.compareTo(other.name)
        return dist.compareTo(other.dist)
    }

    override fun toString() = "($name, $dist)"
}

class Graph(
    edges: List<Edge>,
) {
    // mapping of vertex names to Vertex objects, built from a set of Edges
    private val graph = HashMap<String, Vertex>(edges.size)

    init {
        // one pass to find all vertices
        for (e in edges) {
            if (!graph.containsKey(e.v1)) graph[e.v1] = Vertex(e.v1)
            if (!graph.containsKey(e.v2)) graph[e.v2] = Vertex(e.v2)
        }

        // another pass to set neighbouring vertices
        for (e in edges) {
            graph[e.v1]!!.neighbours[graph[e.v2]!!] = e.dist
        }
    }

    /** Runs dijkstra using a specified source vertex */
    fun dijkstra(startName: String) {
        if (!graph.containsKey(startName)) {
            println("Graph doesn't contain start vertex '$startName'")
            return
        }
        val source = graph[startName]
        val q = TreeSet<Vertex>()

        // set-up vertices
        for (v in graph.values) {
            v.previous = if (v == source) source else null
            v.dist = if (v == source) 0 else Int.MAX_VALUE
            q.add(v)
        }

        dijkstra(q)
    }

    /** Implementation of dijkstra's algorithm using a binary heap */
    private fun dijkstra(q: TreeSet<Vertex>) {
        while (!q.isEmpty()) {
            // vertex with shortest distance (first iteration will return source)
            val u = q.pollFirst()
            // if distance is infinite we can ignore 'u' (and any other remaining vertices)
            // since they are unreachable
            if (u!!.dist == Int.MAX_VALUE) break

            //look at distances to each neighbour
            for (a in u.neighbours) {
                val v = a.key // the neighbour in this iteration

                val alternateDist = u.dist + a.value
                if (alternateDist < v.dist) { // shorter path to neighbour found
                    q.remove(v)
                    v.dist = alternateDist
                    v.previous = u
                    q.add(v)
                }
            }
        }
    }

    /** Prints a path from the source to the specified vertex */
    fun printPath(endName: String) {
        if (!graph.containsKey(endName)) {
            println("Graph doesn't contain end vertex '$endName'")
            return
        }
        graph[endName]!!.printPath()
        println()
    }

    fun printLowestPath(endName: String) {
        if (!graph.containsKey(endName)) {
            println("Graph doesn't contain end vertex '$endName'")
            return
        }
        println(graph[endName]!!.dist)
    }

    fun getLowestPath(endName: String): Int {
        if (!graph.containsKey(endName)) {
            println("Graph doesn't contain end vertex '$endName'")
            return Int.MAX_VALUE
        }
        return graph[endName]!!.dist
    }

}