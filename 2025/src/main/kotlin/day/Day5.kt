package day

import common.DailyChallenge
import common.Input
import java.util.function.Predicate
import kotlin.math.max
import kotlin.math.min

class Day5 : DailyChallenge {
    override fun puzzle1(input: Input): Long {
        val ranges = input.values.takeWhile { !it.isBlank() }
            .map { it.split("-").let { s -> Pair(s.first().toLong(), s.last().toLong()) } }
            .map { r -> Predicate<Long> { it >= r.first && it <= r.second } }
        val ingredientIds = input.values.reversed().takeWhile { !it.isBlank() }.map { it.toLong() }

        return ingredientIds.count { ingredientId -> ranges.any { it.test(ingredientId) } }.toLong()
    }

    override fun puzzle2(input: Input): Long {
        val rangesString = input.values.takeWhile { !it.isBlank() }
        val ranges = rangesString.map { it.split("-").let { s -> Pair(s.first().toLong(), s.last().toLong()) } }
            .map { it.first.rangeTo(it.second) }

        return Ranges(ranges).simplify().size()
    }

    data class Ranges(val rangeList: List<LongRange>) {

        fun size() = rangeList.sumOf { it.endInclusive - it.start + 1 }

        fun simplify(): Ranges {
            var initialRanges: Ranges
            var newRanges = this
            do {
                initialRanges = newRanges
                newRanges = initialRanges.rangeList.drop(1).fold(Ranges(listOf(initialRanges.rangeList.first()))) { ranges, r -> ranges.merge(r) }
            } while (newRanges != initialRanges)
            return newRanges
        }

        private fun merge(r: LongRange): Ranges {
            var rStart = r.start
            var rEnd = r.endInclusive
            var overlapsWithOtherRange = false
            val newRangeList = rangeList.map { range ->
                if (overlapsWithOtherRange) range
                else if (rEnd < range.start || rStart > range.endInclusive) range
                else if (rStart >= range.start && rEnd <= range.endInclusive) {
                    overlapsWithOtherRange = true
                    range
                } else {
                    val newRange = min(rStart, range.start).rangeTo(max(rEnd, range.endInclusive))
                    rStart = if (rStart > range.start) range.endInclusive else rStart
                    rEnd = if (rEnd < range.endInclusive) range.start else rEnd
                    overlapsWithOtherRange = true
                    newRange
                }
            }
            return if (!overlapsWithOtherRange) Ranges(rangeList + listOf(r)) else Ranges(newRangeList)
        }
    }


}