package day

import common.DailyChallenge
import common.Input

class Day1 : DailyChallenge {

    override fun puzzle1(input: Input) = input.split { it.toLong() }.maxOf { l -> l.sum() }

    override fun puzzle2(input: Input) = input.split { it.toLong() }
        .map { l -> l.sum() }
        .sortedDescending()
        .take(3)
        .sum()

}
