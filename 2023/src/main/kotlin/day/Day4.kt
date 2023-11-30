package day

import common.DailyChallenge
import common.Input

class Day4 : DailyChallenge {

    override fun puzzle1(input: Input) = input
        .toPairs(",") { s -> LongRange(s.split("-")[0].toLong(), s.split("-")[1].toLong()) }
        .count { p -> p.first.intersect(p.second).size == minOf(p.first.count(), p.second.count())  }
        .toLong()


    override fun puzzle2(input: Input) = input
        .toPairs(",") { s -> LongRange(s.split("-")[0].toLong(), s.split("-")[1].toLong()) }
        .count { p -> p.first.intersect(p.second).isNotEmpty() }
        .toLong()

}
