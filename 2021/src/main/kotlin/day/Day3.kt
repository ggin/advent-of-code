package day

import common.DailyChallenge
import java.util.stream.Collectors
import java.util.stream.IntStream

class Day3 : DailyChallenge {

    override fun puzzle1(values: List<String>): Long {
        val size = values[0].length
        val gamma = IntStream.range(0, size)
            .mapToObj { mostCommonBitInColumn(values, it) }
            .collect(Collectors.joining())

        val gammaRate = gamma.toLong(2)
        val epsilonRate = flipBits(gamma).toLong(2)

        return gammaRate * epsilonRate
    }

    override fun puzzle2(values: List<String>): Long {
        val size = values[0].length
        val oxygenCandidates = values.toMutableList()
        val co2ScrubberCandidates = values.toMutableList()
        IntStream.range(0, size)
            .forEach { col ->
                val mostCommonBit = mostCommonBitInColumn(oxygenCandidates, col)
                oxygenCandidates.removeAll { oxygenCandidates.size > 1 && it[col].toString() != mostCommonBit }

                val leastCommonBit = leastCommonBitInColumn(co2ScrubberCandidates, col)
                co2ScrubberCandidates.removeAll { co2ScrubberCandidates.size > 1 && it[col].toString() != leastCommonBit }
            }

        val oxygenRating = toDecimal(oxygenCandidates)
        val co2ScrubberRating = toDecimal(co2ScrubberCandidates)
        return oxygenRating * co2ScrubberRating
    }

    private fun flipBits(s: String) =
        s.toCharArray().map { if (it == '1') '0' else '1' }.joinToString("")

    private fun extractColumn(values: List<String>, index: Int) =
        values.map { it.toCharArray()[index].toString().toInt() }

    private fun mostCommonBitInColumn(values: List<String>, index: Int): String {
        val bitSum = extractColumn(values, index).sum()
        return if (values.size % 2 == 0 && bitSum == values.size / 2) {
            "1"
        } else if (bitSum > values.size / 2) {
            "1"
        } else {
            "0"
        }
    }

    private fun leastCommonBitInColumn(values: List<String>, index: Int) =
        flipBits(mostCommonBitInColumn(values, index))

    private fun toDecimal(v: List<String>) = v.joinToString("").toLong(2)
}
