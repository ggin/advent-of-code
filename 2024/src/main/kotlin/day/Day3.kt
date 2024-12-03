package day

import common.DailyChallenge
import common.Input

class Day3 : DailyChallenge {

    private val regex = "(?<=mul\\((\\d{1,3}),(\\d{1,3})\\))|(do\\(\\))|(don't\\(\\))".toRegex()

    override fun puzzle1(input: Input) = regex.findAll(input.content)
        .map { it.groupValues }
        .filter { it[1].isNotEmpty() }
        .sumOf { it[1].toLong() * it[2].toLong() }

    override fun puzzle2(input: Input): Long {
        var factor = 1;
        return regex.findAll(input.content.replace(")do", ") do")) // the current regex cannot capture both groups for string mul(X,X)do(), adding a space does the trick
            .map { it.groupValues }
            .filter {
                if (it[3].isNotEmpty()) factor = 1
                if (it[4].isNotEmpty()) factor = 0
                it[1].isNotEmpty()
            }
            .sumOf { factor * it[1].toLong() * it[2].toLong() }
    }

}