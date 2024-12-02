package day

import common.DailyChallenge
import common.Input
import kotlin.math.abs

class Day1 : DailyChallenge {
    override fun puzzle1(input: Input): Long {
        val leftPairs = input.values.map { it.split("   ")[0].toLong() }.sorted()
        val rightPairs = input.values.map { it.split("   ")[1].toLong() }.sorted()
        return leftPairs.zip(rightPairs).map { abs( it.second - it.first) }.sum()
    }

    override fun puzzle2(input: Input): Long {
        val leftPairs = input.values.map { it.split("   ")[0].toLong() }.sorted()
        val rightPairs = input.values.map { it.split("   ")[1].toLong() }.sorted()
        return leftPairs.sumOf { left -> rightPairs.count { it == left } * left }
    }
}