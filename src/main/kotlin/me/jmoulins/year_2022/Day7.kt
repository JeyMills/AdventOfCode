package me.jmoulins.year_2022

fun main() {
    val input = java.io.File("src/main/resources/year_2022/day7/input.txt").readLines()
    val root = followInstructions(input)
    root.print()
    println("Part 1 - Total size of directories < 100000: ${root.totalSizeOfAllDirectories(100000)}")
    println("Root size: ${root.totalSizeOfFiles()}")
    val totalDiskSpace = 70000000L
    val spaceNeeded = 30000000L
    val spaceLeft = totalDiskSpace - root.size
    val spaceToFree = spaceNeeded - spaceLeft
    val candidates = root.findCandidateDirectoriesToDelete(spaceToFree)
    println("Part 2 - Smallest directory that free enough space has size: ${candidates.minOf { it.size }}")
}

fun followInstructions(input: List<String>): File {
    val root = File(name = "/", isDirectory = true)
    var currentDir: File? = root
    input.forEach { line ->
        with(line) {
            when {
                equals("$ cd /") || equals("$ ls") -> {}
                startsWith("dir") -> currentDir!!.children.add(
                    File(
                        name = line.drop(4),
                        isDirectory = true,
                        parent = currentDir
                    )
                )
                startsWith("$ cd") -> {
                    currentDir = if (line == "$ cd ..") {
                        currentDir!!.calculateDirectorySize()
                        currentDir!!.parent!!
                    } else {
                        currentDir!!.children.first { it.name == line.drop(5) }
                    }
                }
                matches("""\d+.*""".toRegex()) -> {
                    val (size, name) = line.split(" ")
                    currentDir!!.children.add(File(name = name, size = size.toLong(), parent = currentDir))
                }
            }
        }
    }
    while (currentDir != null) {
        currentDir!!.calculateDirectorySize()
        currentDir = currentDir!!.parent
    }
    return root
}

class File(
    var name: String,
    var isDirectory: Boolean = false,
    var size: Long = 0,
    val children: MutableList<File> = mutableListOf(),
    var parent: File? = null
) {
    fun calculateDirectorySize() {
        this.size = children.sumOf { it.size }
    }

    fun totalSizeOfAllDirectories(maxSize: Long): Long {
        var totalSize = 0L
        if (this.isDirectory && this.size <= maxSize) {
            totalSize += this.size
        }
        totalSize += this.children.sumOf { it.totalSizeOfAllDirectories(maxSize) }
        return totalSize
    }

    fun findCandidateDirectoriesToDelete(spaceToFree: Long): List<File> {
        val candidates = mutableListOf<File>()
        if (this.isDirectory && this.size >= spaceToFree) {
            candidates.add(this)
            candidates.addAll(this.children.flatMap { it.findCandidateDirectoriesToDelete(spaceToFree) })
        }
        return candidates
    }

    fun print(depth: Int = 0) {
        for (i in 1..depth) {
            print("\t")
        }
        println("- $name (${if (isDirectory) "dir" else "file"}, size=$size)")
        this.children.forEach { it.print(depth + 1) }
    }

    fun totalSizeOfFiles(): Long {
        return (if (this.isDirectory) 0L else this.size) + this.children.sumOf { it.totalSizeOfFiles() }
    }

}