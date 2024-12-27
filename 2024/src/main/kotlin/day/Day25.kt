package day

import common.DailyChallenge
import common.Input

class Day25 : DailyChallenge {
    override fun puzzle1(input: Input): Long {
        val locks = mutableListOf<List<Int>>()
        val keys = mutableListOf<List<Int>>()
        input.values.chunked(8).forEach { block ->
            val heights = (0..4).map { column ->
                (1..5).count { row -> block[row][column] == '#' }
            }
            if (block.first().first() == '#') locks += heights
            else keys += heights
        }
        return locks.sumOf { lock ->
            keys.count { key ->
                (lock zip key).none { it.first + it.second > 5}
            }
        }.toLong()
    }

    override fun puzzle2(input: Input): Long {
        TODO()
    }
}