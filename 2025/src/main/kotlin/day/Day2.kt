package day

import common.DailyChallenge
import common.Input

class Day2 : DailyChallenge {
    override fun puzzle1(input: Input): Long {
        return input.content.split(",")
            .flatMap { it.split("-").let { sp -> sp[0].toLong()..sp[1].toLong() } }
            .filter { it.toString().let{ s -> s.substring(0, s.length/2) == s.substring(s.length/2) }}
            .sum()
    }

    override fun puzzle2(input: Input): Long {
        return input.content.split(",")
            .flatMap { it.split("-").let { sp -> sp[0].toLong()..sp[1].toLong() } }
            .filter { isInvalidIdPart2(it.toString()) }
            .sum()
    }

    fun isInvalidIdPart2(s: String): Boolean {
        return (1..s.length/2).any { isInvalidIdPart2(s, it) }
    }

    fun isInvalidIdPart2(s: String, splitIndex: Int): Boolean {
        val chunked = s.chunked(splitIndex)
        return chunked.all { it == chunked[0] }
    }

    /*
    Puzzle 1: 18700015741
Time taken: 84.403209ms
Puzzle 2: 20077272987
Time taken: 540.652916ms
     */
}