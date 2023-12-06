package day

import common.DailyChallenge
import common.Input
import java.util.*

class Day5 : DailyChallenge {

    /*
    Puzzle 1: 265018614
Puzzle 2: 63179500
     */
    private val mapRegex = "(\\w+)-to-(\\w+) map:".toRegex()

    private data class M(val fromCat: String, val toCat: String, val ranges: MutableList<Range> = mutableListOf()) {
        internal fun map(n: Long) = ranges.firstNotNullOfOrNull { it.map(n) } ?: n
    }

    private data class Range(val sourceFrom: Long, val destFrom: Long, val length: Long) {
        internal fun map(n: Long) = if (n >= sourceFrom && n < sourceFrom + length) destFrom + n - sourceFrom else null
    }

    private fun parseMaps(text: List<String>): List<M> {
        val maps = mutableListOf<M>()
        var fromCat = ""
        var toCat = ""
        lateinit var m: M
        text.filter { it.isNotEmpty() }
            .forEach { line ->
                if (mapRegex.matches(line)) {
                    fromCat = mapRegex.find(line)!!.groupValues[1]
                    toCat = mapRegex.find(line)!!.groupValues[2]
                    m = M(fromCat, toCat)
                    maps += m
                } else {
                    val split = line.split(" ")
                    m.ranges += Range(split[1].toLong(), split[0].toLong(), split[2].toLong())
                }
            }
        return maps
    }

    private fun parseSeeds(text: String) = text.split(":")[1]
        .split(" ")
        .filterNot { it.isEmpty() }
        .map { it.toLong() }

    /*    private fun parseSeedRanges(text: String) = text.split(":")[1]
                .split(" ")
                .filterNot { it.isEmpty() }
                .chunked(2)
                .flatMap { it[0].toLong() until it[0].toLong() + it[1].toInt() }
                .distinct() */

    private fun parseSeedRanges(text: String) = text.split(":")[1]
        .split(" ")
        .filterNot { it.isEmpty() }
        .chunked(2)
        .map { LongRange(it[0].toLong(), it[0].toLong() + it[1].toInt() - 1) }

    private fun calculateLocation(seed: Long, maps: List<M>): Long {
        var destination = maps.first { it.fromCat == "seed" }
        var v = seed
        while (destination.toCat != "location") {
            v = destination.map(v)
            destination = maps.first { it.fromCat == destination.toCat }
        }
        return destination.map(v)
    }

    // 79753136
    override fun puzzle1(input: Input): Long {
        val seeds = parseSeeds(input.values.first())
        val maps = parseMaps(input.values.drop(1))
        return seeds.minOf { calculateLocation(it, maps) }
    }

    override fun puzzle2(input: Input): Long {
        val seedRanges = parseSeedRanges(input.values.first())
        val maps = parseMaps(input.values.drop(1))
        return seedRanges.minOf { range -> range.minOf { calculateLocation(it, maps) } }
    }


}
