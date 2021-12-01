package day

import common.DailyChallenge

class Day1 : DailyChallenge {

    override fun puzzle1(values: List<String>): Long {
        val input = toLongValues(values)
        return input.filterIndexed { index, x ->
            index != input.size - 1 && x < input[index + 1]
        }.count().toLong()
    }

    override fun puzzle2(values: List<String>): Long {
        val input = toLongValues(values)
        return input.filterIndexed { index, x ->
            index < input.size - 1  && index > 1 && input[index - 2] < input[index + 1]
        }.count().toLong()
    }
}
