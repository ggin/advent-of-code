package day

import common.DailyChallenge
import common.Input

class Day2 : DailyChallenge {
    override fun puzzle1(input: Input): Long {
        return input.values
            .map { it.split(" ").map { it.toLong() }.toList() }
            .count { isSafe(it) || isSafe(it.reversed()) }
            .toLong()
    }

    private fun isSafe(l: List<Long>): Boolean {
        if (l.size == 1)
            return true
        if (l[1] - l[0] in 1..3)
            return isSafe(l.drop(1))
        return false
    }

    override fun puzzle2(input: Input): Long {
        return input.values
            .map { it.split(" ").map { it.toLong() }.toList() }
            .count { isSafeWithTolerance(it, 1, true) || isSafeWithTolerance(it.reversed(), 1, true) }
            .toLong()
    }

    private fun isSafeWithTolerance(l: List<Long>, tolLevel: Int, isFirstRecursion: Boolean = false): Boolean {
        if (tolLevel == -1)
            return false
        if (l.size == 1)
            return true
        return if (l[1] - l[0] in 1..3)
            isSafeWithTolerance(l.drop(1), tolLevel)
        else if (isFirstRecursion)
            isSafeWithTolerance(listOf(l.first()) + l.drop(2), tolLevel - 1) || isSafeWithTolerance(l.drop(1), tolLevel - 1)
        else
            isSafeWithTolerance(listOf(l.first()) + l.drop(2), tolLevel - 1)
    }
}