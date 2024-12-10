package day

import common.DailyChallenge
import common.Input

class Day2 : DailyChallenge {
    override fun puzzle1(input: Input) = input.toLongList(" ")
        .count { isSafe(it) || isSafe(it.reversed()) }
        .toLong()

    override fun puzzle2(input: Input) = input.toLongList()
        .count {
            isSafe(it, 1)
                    || isSafe(it.drop(1), 0)
                    || isSafe(it.reversed(), 1)
                    || isSafe(it.reversed().drop(1), 0)
        }
        .toLong()

    private tailrec fun isSafe(l: List<Long>, tolLevel: Int = 0): Boolean {
        if (tolLevel == -1)
            return false
        if (l.size == 1)
            return true
        return if (l[1] - l[0] in 1..3)
            isSafe(l.drop(1), tolLevel)
        else
            isSafe(listOf(l.first()) + l.drop(2), tolLevel - 1)
    }
}