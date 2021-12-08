package day

import common.DailyChallenge
import kotlin.math.abs

class Day7 : DailyChallenge {

    override fun puzzle1(values: List<String>): Long {
        val positions = values[0].split(",").map { it.toInt() }
        val min = positions.minOrNull()!!
        val max = positions.maxOrNull()!!
        return (min..max).minOf { positionCandidate ->
            positions.sumOf { abs(positionCandidate - it) }
        }.toLong()
    }

    override fun puzzle2(values: List<String>): Long {
        val positions = values[0].split(",").map { it.toInt() }
        val min = positions.minOrNull()!!
        val max = positions.maxOrNull()!!
        return (min..max).minOf { positionCandidate ->
            positions.sumOf { (1..abs(positionCandidate - it)).fold(0) { i: Int, j: Int -> i + j } }
        }.toLong()
    }

}
