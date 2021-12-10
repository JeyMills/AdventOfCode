package me.jmoulins.year_2021

class Day8 {

    companion object {
        @JvmStatic
        fun main(vararg args: String) {
            step1(readFileUsingGetResource())
            step2(readFileUsingGetResource())
        }

        private fun step1(lines: List<String>) {
            println("STEP 1")
            val count: Int = lines.sumOf { s ->
                s.split("|")[1].trim().split(" ")
                    .count { it.length == 2 || it.length == 3 || it.length == 4 || it.length == 7 }
            }
            println("Digits 1, 4, 7, or 8 appear $count times")
            println()
        }

        private fun step2(lines: List<String>) {
            println("STEP 2")
            val digits: Map<String, Int> = mapOf(
                Pair("abcdef", 0),
                Pair("bc", 1),
                Pair("abdeg", 2),
                Pair("abcdg", 3),
                Pair("bcfg", 4),
                Pair("acdfg", 5),
                Pair("acdefg", 6),
                Pair("abc", 7),
                Pair("abcdefg", 8),
                Pair("abcdfg", 9),
            )

            var sum = 0
            lines.forEach {
                val entry = it.split(" | ")
                val patterns = entry.first().split(" ")
                val values = entry[1].split(" ")

                // We determine 1 and 7 thanks to their length
                val one = patterns.first { s -> s.length == 2 }
                val seven = patterns.first { s -> s.length == 3 }

                // Deduction of a
                val a = seven
                    .replace(one.first().toString(), "")
                    .replace(one[1].toString(), "")

                // We determine 4 thanks to its length
                val four = patterns.first { s -> s.length == 4 }

                // Deduction of fg
                val fg = four
                    .replace(one.first().toString(), "")
                    .replace(one[1].toString(), "")

                // We determine 5 thanks to its length and fg
                val five = patterns
                    .filter { s -> s.length == 5 }
                    .filter { s -> s.contains(fg.first()) }
                    .first { s -> s.contains(fg[1]) }

                // Deduction of cd
                val cd = five
                    .replace(a, "")
                    .replace(fg.first().toString(), "")
                    .replace(fg[1].toString(), "")

                // We determine 3 thanks to its length and the acd
                val three = patterns
                    .asSequence()
                    .filter { s -> s.length == 5 }
                    .filter { s -> s !== five }
                    .filter { s -> s.contains(a) }
                    .filter { s -> s.contains(cd.first()) }
                    .first { s -> s.contains(cd[1]) }

                // Deduction of b,f,g
                val bg = three
                    .replace(a, "")
                    .replace(cd.first().toString(), "")
                    .replace(cd[1].toString(), "")
                val b = bg
                    .replace(fg.first().toString(), "")
                    .replace(fg[1].toString(), "")
                val g = bg.replace(b, "")
                val f = fg.replace(g, "")

                // We determine six thanks to its length and bcd
                val six = patterns
                    .filter { s -> s.length == 6 }
                    .first { s -> !(s.contains(b) && s.contains(cd.first()) && s.contains(cd[1])) }

                // Deduction of e
                val e = six
                    .replace(a, "")
                    .replace(cd.first().toString(), "")
                    .replace(cd[1].toString(), "")
                    .replace(f, "")
                    .replace(g, "")

                // Deduction of c
                val c = one.replace(b, "")

                // Deduction of d
                val d = cd.replace(c, "")

                val transform: Map<String, String> = mapOf(
                    Pair(a, "a"),
                    Pair(b, "b"),
                    Pair(c, "c"),
                    Pair(d, "d"),
                    Pair(e, "e"),
                    Pair(f, "f"),
                    Pair(g, "g")
                )

                val thousands = digits[values.first().map { v -> transform[v.toString()]!! }.sorted().joinToString("")]
                val hundreds = digits[values[1].map { v -> transform[v.toString()]!! }.sorted().joinToString("")]
                val tens = digits[values[2].map { v -> transform[v.toString()]!! }.sorted().joinToString("")]
                val units = digits[values[3].map { v -> transform[v.toString()]!! }.sorted().joinToString("")]

                sum += thousands!! * 1000 + hundreds!! * 100 + tens!! * 10 + units!!
            }
            println("Sum is $sum")
        }

        private fun readFileUsingGetResource(): List<String> {
            return this::class.java.getResourceAsStream("/year_2021/day8/input.txt")?.bufferedReader()?.readLines() ?: emptyList()
        }
    }
}