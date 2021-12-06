package day

import common.DailyChallenge
import java.util.stream.IntStream

class Day6 : DailyChallenge {

    override fun puzzle1(values: List<String>) = numberFishAfterXDays(values, 80)

    override fun puzzle2(values: List<String>) = numberFishAfterXDays(values, 256)

    private fun numberFishAfterXDays(values: List<String>, nbDays: Int): Long {
        var countByTimer = countByTimer(values)
        IntStream.range(0, nbDays).forEach { _ ->
            countByTimer = countByTimer.map { (timer, count) ->
                val newTimer = if (timer == 0) 6 else timer - 1
                if (newTimer == 0)
                    listOf(Pair(newTimer, count), Pair(9, count))
                else
                    listOf(Pair(newTimer, count))
            }.flatten()
                .groupBy({ it.first }, { it.second })
                .mapValues { (_, values) -> values.sum() }
        }
        return countByTimer.filterKeys { it <= 8 }.values.sum()
    }

    private fun countByTimer(values: List<String>): Map<Int, Long> {
        return values[0].split(",").map { it.toInt() }.groupingBy { it }.eachCount()
            .mapValues { it.value.toLong() }
    }

}
