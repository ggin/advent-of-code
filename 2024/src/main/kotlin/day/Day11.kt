package day

import common.DailyChallenge
import common.Input

class Day11 : DailyChallenge {
    override fun puzzle1(input: Input): Long {
        val values = input.singleValue.split(" ").map { it.toLong() }
        val m = values.associateWith { 1L }
        return (1..25).fold(m) { acc, _ -> blink(acc) }.values.sum()
    }

    private fun blink(l: Map<Long, Long>): Map<Long, Long> {
        return l.flatMap { (value, count) ->
            if (value == 0L) listOf(1L to count)
            else if (value.toString().length % 2 == 0) {
                val half = value.toString().length / 2
                val first = value.toString().substring(0, half).toLong()
                val second = value.toString().substring(half).toLong()
                listOf(first to count, second to count)
            } else listOf(value * 2024 to count)
        }.groupingBy { it.first }.fold(0L) { acc, (_, count) -> acc + count }
    }

    override fun puzzle2(input: Input): Long {
        val values = input.singleValue.split(" ").map { it.toLong() }
        val m = values.associateWith { 1L }
        return (1..75).fold(m) { acc, _ -> blink(acc) }.values.sum()
    }

}