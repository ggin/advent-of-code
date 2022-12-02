package day

import common.DailyChallenge
import common.Input

class Day2 : DailyChallenge {

    override fun puzzle1(input: Input) = input.toPairs { s -> toShape(s[0]) }
        .sumOf { shapeScore(it.second) + roundScore(it.first, it.second) }

    override fun puzzle2(input: Input) = input.toPairs { s -> toShape(s[0]) }
        .sumOf { shapeScore(findShape(it.first, it.second)) + roundScore(it.second) }

    private fun toShape(c: Char) = (c - 'A') % 23 // A=X=0, B=Y=1, C=Z=2
    private fun shapeScore(s: Int) = s + 1
    private fun roundScore(p1: Int, p2: Int) = (p2 - p1 + 1).mod(3) * 3L
    private fun findShape(p1: Int, outcome: Int) = (p1 + outcome - 1).mod(3)

    private fun roundScore(outcome: Int) = outcome * 3L
}
