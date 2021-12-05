package day

import common.DailyChallenge
import java.util.function.BiFunction
import java.util.stream.IntStream
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.streams.toList

class Day5 : DailyChallenge {

    override fun puzzle1(values: List<String>): Long {
        val map: MutableMap<Pair<Int, Int>, Long> = mutableMapOf()
        val segments: MutableList<Segment> = mutableListOf()
        values.forEach { line ->
            segments.add(Segment(line))
        }
        segments.filter { it.isHorizontalLine() }.map { it.getCoordinatesForHorizontalLine() }
            .flatten()
            .forEach { map.merge(it, 1L) { i, j -> i + j } }

        segments.filter { it.isVerticalLine() }.map { it.getCoordinatesForVerticalLine() }
            .flatten()
            .forEach { map.merge(it, 1L) { i, j -> i + j } }

        return map.filterValues { it > 1 }.count().toLong()
    }

    override fun puzzle2(values: List<String>): Long {
        val map: MutableMap<Pair<Int, Int>, Long> = mutableMapOf()
        val segments: MutableList<Segment> = mutableListOf()
        values.forEach { line ->
            segments.add(Segment(line))
        }
        segments.filter { it.isHorizontalLine() }.map { it.getCoordinatesForHorizontalLine() }
            .flatten()
            .forEach { map.merge(it, 1L) { i, j -> i + j } }

        segments.filter { it.isVerticalLine() }.map { it.getCoordinatesForVerticalLine() }
            .flatten()
            .forEach { map.merge(it, 1L) { i, j -> i + j } }

        segments.filter { it.isDiagonalLine() }.map { it.getCoordinatesForDiagonalLine() }
            .flatten()
            .forEach { map.merge(it, 1L) { i, j -> i + j } }

        return map.filterValues { it > 1 }.count().toLong()
    }

    class Segment(line: String) {
        private val start: Pair<Int, Int> = toPair(line.split(" -> ")[0])
        private val end: Pair<Int, Int> = toPair(line.split(" -> ")[1])

        private fun toPair(s: String) = s.split(",").let { Pair(it[0].toInt(), it[1].toInt()) }

        fun firstOffset() = (end.first - start.first) / (max(abs(end.first - start.first), 1))
        fun secondOffset() = (end.second - start.second) / (max(abs(end.second - start.second), 1))
        fun isHorizontalLine() = start.second == end.second
        fun isVerticalLine() = start.first == end.first
        fun isDiagonalLine() = !isVerticalLine() && !isHorizontalLine()

        fun getCoordinatesForHorizontalLine() =
            IntStream.rangeClosed(min(start.first, end.first), max(start.first, end.first))
                .mapToObj { Pair(it, start.second) }
                .toList()

        fun getCoordinatesForVerticalLine() =
            IntStream.rangeClosed(min(start.second, end.second), max(start.second, end.second))
                .mapToObj { Pair(start.first, it) }
                .toList()

        fun getCoordinatesForDiagonalLine() =
            IntStream.rangeClosed(0, abs(start.first - end.first))
                .mapToObj { Pair(
                    if (start.first < end.first) start.first + it else start.first - it,
                    if (start.second < end.second) start.second + it else start.second - it,
                )}
                .toList()

    }

}
