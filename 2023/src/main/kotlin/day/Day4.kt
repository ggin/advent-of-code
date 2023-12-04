package day

import common.DailyChallenge
import common.Input
import kotlin.math.pow

class Day4 : DailyChallenge {

    private val regex = "Card\\s+\\d+: ([\\d ]+)".toRegex()

    data class Row(val winningNumbers: List<Int>, val numbers: List<Int>, var occurrences: Int = 1) {
        internal fun score(): Long {
            val count = count()
            return if (count == 0) return 0 else (2.0).pow(count - 1).toLong()
        }

        internal fun count() = winningNumbers.count { numbers.contains(it) }
        internal fun increment() = occurrences++
    }

    override fun puzzle1(input: Input): Long {
        return parseRows(input).sumOf { it.score() }
    }


    override fun puzzle2(input: Input): Long {
        val rows = parseRows(input)
        rows.forEachIndexed { index, row ->
            (1..row.occurrences).forEach { _ ->
                (1..row.count()).forEach {
                    if (index + it < rows.size) rows[index + it].increment()
                }
            }
        }
        return rows.sumOf { it.occurrences }.toLong()
    }

    private fun parseRows(input: Input) = input.values.map {
        val split = it.split("|")
        val winningNumbers = regex.find(split[0])!!.groupValues[1].trim().split(" ").filter { s -> s.isNotEmpty() }.map { s -> s.trim().toInt() }
        val numbers = split[1].trim().split(" ").filter { s -> s.isNotEmpty() }.map { s -> s.trim().toInt() }
        Row(winningNumbers, numbers)
    }

}
