package day

import common.DailyChallenge
import common.Input

class Day3 : DailyChallenge {

    override fun puzzle1(input: Input) = input.values
        .map { it.chunked(it.length / 2) }
        .map { commonChars(it[0], it[1]).first() }
        .sumOf { toPriority(it) }

    override fun puzzle2(input: Input) = input.values
        .chunked(3)
        .map { commonChars(commonChars(it[0], it[1]).toString(), it[2]).first() }
        .sumOf { toPriority(it) }

    private fun commonChars(s1: String, s2: String) = s1.toSet().intersect(s2.toSet())
    private fun toPriority(c: Char) = (c - '`').mod(58L)
}
