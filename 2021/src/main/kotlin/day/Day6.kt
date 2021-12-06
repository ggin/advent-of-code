package day

import common.DailyChallenge

class Day6 : DailyChallenge {

    override fun puzzle1(values: List<String>) = numberFishAfterXDays(values, 80)

    override fun puzzle2(values: List<String>) = numberFishAfterXDays(values, 256)

    private fun numberFishAfterXDays(values: List<String>, nbDays: Int): Long {
        val countByTimer = countByTimer(values)

        return (0 until nbDays).toList().foldRight(countByTimer) { _, c ->
            c.map { (timer, count) ->
                if (timer == 0)
                    listOf(Pair(6, count), Pair(8, count))
                else
                    listOf(Pair(timer - 1, count))
            }.flatten()
                .groupBy({ it.first }, { it.second })
                .mapValues { (_, values) -> values.sum() }
        }.values.sum()
    }

    private fun countByTimer(values: List<String>): Map<Int, Long> {
        return values[0].split(",").map { it.toInt() }.groupingBy { it }.eachCount()
            .mapValues { it.value.toLong() }
    }

}
