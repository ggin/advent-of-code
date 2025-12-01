package day

import common.DailyChallenge
import common.Input
import kotlin.math.abs

class Day1 : DailyChallenge {
    override fun puzzle1(input: Input): Long {
        return input.values.runningFold(50) { dial, s ->
            (dial + (if (s.first() == 'L') -1 else 1) * s.drop(1).toInt()).mod(100)
        }.count { it == 0 }.toLong()
    }

    data class Result(val dial: Int, val count: Long)

    override fun puzzle2(input: Input): Long {
        return input.values.fold(Result(50, 0)) { r, s ->
            val newResult = r.dial + (if (s.first() == 'L') -1 else 1) * s.drop(1).toInt()
            val newResultMod = newResult.mod(100)
            val rForDiv = if (newResult < 0) abs(newResult) + (if (r.dial == 0) 0 else 100) else newResult
            val countDelta = if (newResultMod == 0 && newResult == 0) 1
            else if (newResultMod == 0 || newResult != newResultMod) rForDiv.div(100)
            else 0
            Result(newResultMod, r.count + countDelta)
        }.count
    }

}