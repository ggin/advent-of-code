package day

import common.DailyChallenge
import common.Input

class Day2Lookup : DailyChallenge {

    val puzzle1Lookup = mapOf(
        "A X" to 4L,
        "A Y" to 8L,
        "A Z" to 3L,
        "B X" to 1L,
        "B Y" to 5L,
        "B Z" to 9L,
        "C X" to 7L,
        "C Y" to 2L,
        "C Z" to 6L
    )
    val puzzle2Lookup = mapOf(
        "A X" to 3L,
        "A Y" to 4L,
        "A Z" to 8L,
        "B X" to 1L,
        "B Y" to 5L,
        "B Z" to 9L,
        "C X" to 2L,
        "C Y" to 6L,
        "C Z" to 7L
    )
    override fun dayNumber() = 2

    override fun puzzle1(input: Input) = input.values.sumOf { puzzle1Lookup[it]!! }
    override fun puzzle2(input: Input) = input.values.sumOf { puzzle2Lookup[it]!! }

}
