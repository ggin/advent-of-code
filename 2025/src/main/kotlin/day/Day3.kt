package day

import common.DailyChallenge
import common.Input
import common.indexesOf

class Day3 : DailyChallenge {
    override fun puzzle1(input: Input) = input.toIntList().sumOf { jollyJoltage(it, 2) }

    override fun puzzle2(input: Input) = input.toIntList().sumOf { jollyJoltage(it, 12) }

    private fun jollyJoltage(l: List<Int>, batteryCount: Int, acc: String = ""): Long {
        if (acc.length == batteryCount) return acc.toLong()
        val subList = l.dropLast(batteryCount - acc.length - 1)
        val firstDigit = subList.max()
        return subList.indexesOf(firstDigit)
            .maxOf { idx -> jollyJoltage(l.drop(idx + 1), batteryCount, acc + firstDigit) }
    }
}