package day

import common.DailyChallenge
import common.Input

class Day7 : DailyChallenge {

    override fun puzzle1(input: Input) = solve(input, listOf(Long::plus, Long::times))
    override fun puzzle2(input: Input) = solve(input, listOf(Long::plus, Long::times, { a: Long, b: Long -> "$a$b".toLong() }))

    private fun solve(input: Input, operations: List<(Long, Long) -> Long>): Long {
        return input.values.sumOf {
            val result = it.split(":")[0].toLong()
            val numbers = it.split(":")[1].trim().split(" ").map { it.toLong() }
            if (validEquation(result, numbers.first(), numbers.drop(1), operations)) result else 0
        }
    }

    private fun validEquation(result: Long, acc: Long, remainingNumbers: List<Long>, operations: List<(Long, Long) -> Long>): Boolean {
        if (remainingNumbers.isEmpty()) return acc == result
        if (acc > result) return false // early exit
        return operations.any { validEquation(result, it(acc, remainingNumbers.first()), remainingNumbers.drop(1), operations) }
    }
}
