package day

import common.DailyChallenge
import common.Input

class Day6 : DailyChallenge {

    override fun puzzle1(input: Input) = solve(input.singleValue, 4).toLong()

    override fun puzzle2(input: Input) = solve(input.singleValue, 14).toLong()

    private fun solve(s: String, windowSize: Int) = s.windowedSequence(windowSize, 1)
        .map { it.toSet().size == it.length }
        .indexOfFirst { it } + windowSize

}
