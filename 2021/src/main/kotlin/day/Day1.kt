package day

import common.DailyChallenge

class Day1 : DailyChallenge {

    override fun puzzle1(values: List<String>) = countLargerMeasurements(toLongValues(values), 1)

    override fun puzzle2(values: List<String>) = countLargerMeasurements(toLongValues(values), 3)

    private fun countLargerMeasurements(measurements: List<Long>, indexOffset: Int): Long {
        return measurements.dropLast(indexOffset)
            .mapIndexed { index, _ -> Pair(index, index + indexOffset) }
            .count { (i, j) -> measurements[i] < measurements[j] }.toLong()
    }
}
