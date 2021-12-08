package day

import common.DailyChallenge

class Day8 : DailyChallenge {

    companion object {
        val UNIQUE_SEGMENTS = setOf(2, 3, 4, 7)
    }

    override fun puzzle1(values: List<String>): Long {
        return values.map { it.split("|")[1] }
            .sumOf { it.split(" ").count { s -> UNIQUE_SEGMENTS.contains(s.length) } }.toLong()
    }

    override fun puzzle2(values: List<String>): Long {
        return values.sumOf {
            val dictionary = createDictionary(parseAndSortAlphabetically(it.split("|")[0]))
            val output = parseAndSortAlphabetically(it.split("|")[1])
            output.map { s -> dictionary[s]!! }.joinToString("").toLong()
        }
    }

    private fun parseAndSortAlphabetically(s: String) =
        s.split(" ").filter { it.isNotEmpty() }.map { sortAlphabetically(it) }

    private fun sortAlphabetically(s: String) = s.toSortedSet().joinToString("")

    private fun createDictionary(values: List<String>): Map<String, Long> {
        val fiveSegments = values.filter { it.length == 5 }.toSet()
        val sixSegments = values.filter { it.length == 6 }.toSet()

        val one = values.first { it.length == 2 }
        val seven = values.first { it.length == 3 }
        val four = values.first { it.length == 4 }
        val eight = values.first { it.length == 7 }
        val two = fiveSegments.first { intersect(it, four).size == 2 }
        val three = fiveSegments.first { intersect(it, one).size == 2 }
        val five = fiveSegments.first { it != two && it != three }
        val nine = sixSegments.first { intersect(it, three).size == 5 }
        val zero = sixSegments.first { intersect(it, five).size == 4 }
        val six = sixSegments.first { it != nine && it != zero }

        return mapOf(
            zero to 0,
            one to 1,
            two to 2,
            three to 3,
            four to 4,
            five to 5,
            six to 6,
            seven to 7,
            eight to 8,
            nine to 9
        )
    }

    private fun intersect(s1: String, s2:String) = s1.toSet().intersect(s2.toSet())


}
