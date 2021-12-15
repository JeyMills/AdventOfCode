package me.jmoulins.year_2021

import java.io.File
import java.util.*
import kotlin.collections.HashMap

fun main() {
    val inputTest = readFileDay15("src/main/resources/year_2021/day15/input-test.txt")
    val input = readFileDay15("src/main/resources/year_2021/day15/input.txt")

    // Part 1
    println("Part 1")
    val start = "(0,0)"

    println("input-test.txt")
    shortestPath(start, "(9,9)", convertToGraph(inputTest))
    println("input.txt")
    shortestPath(start, "(99,99)", convertToGraph(input))

    // Part 2
    println("Part 2")
    println("input-test.txt")
    shortestPath(start, "(49,49)", convertToGraph(fiveTimesLarger(inputTest)))
    println("input.txt")
    shortestPath(start, "(499,499)", convertToGraph(fiveTimesLarger(input)))
}

fun shortestPath(start: String, end: String, edges: List<Edge>) {
    with(Graph(edges)) {
        dijkstra(start)
        printPath(end)
        printLowestRisk(end)
        println()
    }
}

fun convertToGraph(input: Array<out IntArray>): List<Edge> {
    val edges: MutableList<Edge> = mutableListOf()
    for (j in input.indices) {
        for (i in input[j].indices) {
            if (i != 0) {
                edges.add(Edge("(${i - 1},$j)", "($i,$j)", input[i][j]))
                edges.add(Edge("($i,$j)", "(${i - 1},$j)", input[i - 1][j]))
            }
            if (j != 0) {
                edges.add(Edge("($i,${j - 1})", "($i,$j)", input[i][j]))
                edges.add(Edge("($i,$j)", "($i,${j - 1})", input[i][j - 1]))
            }
        }
    }
    return edges
}

fun readFileDay15(path: String): Array<out IntArray> {
    return File(path)
        .readLines()
        .map { s -> s.chunked(1).map { it.toInt() }.toIntArray() }
        .toTypedArray()
}

fun fiveTimesLarger(input: Array<out IntArray>): Array<out IntArray> {
    val largerArray = Array(5 * input.size) { IntArray(5 * input.first().size) }
    for (j in largerArray.indices) {
        for (i in largerArray[j].indices) {
            largerArray[i][j] =
                (input[i % input.first().size][j % input.size] + i / input.first().size + j / input.size - 1) % 9 + 1
        }
    }
    return largerArray
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
            if (u.dist == Int.MAX_VALUE) break

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

    fun printLowestRisk(endName: String) {
        if (!graph.containsKey(endName)) {
            println("Graph doesn't contain end vertex '$endName'")
            return
        }
        println(graph[endName]!!.dist)
    }

}