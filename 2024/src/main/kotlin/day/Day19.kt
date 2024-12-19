package day

import common.DailyChallenge
import common.Input

class Day19 : DailyChallenge {
    override fun puzzle1(input: Input): Long {
        val towels = input.values.first().split(",").map { it.trim() }
        val designs = input.values.drop(2).map { it.trim() }
        val cache = buildCache(towels, designs)
        return designs.count { isDesignPossibleCache(it, cache, 1) > 0L }.toLong()
    }

    private fun isDesignPossible(design: String, towels: List<String>): Boolean {
        return (1..100).any {
            val shuffledTowels = towels.shuffled()
            var oldRemaining = ""
            var remaining = design
            while (remaining != oldRemaining && remaining != "X".repeat(design.length)) {
                oldRemaining = remaining
                remaining = shuffledTowels.foldRight(remaining) { towel, acc -> acc.replace(towel, "X".repeat(towel.length)) }
            }
            remaining == "X".repeat(design.length)
        }
    }

    private fun buildCache(towels: List<String>, designs: List<String>): Map<String, Long> {
        val minSize = designs.minOf { it.length }
        val maxSize = designs.maxOf { it.length }
        val finalCache = mutableMapOf<String, Long>()
        val minTowelLength = towels.minOf { it.length }
        val maxTowelLength = towels.maxOf { it.length }
        var cache = towels.filter { it.length == minTowelLength }.associateWith { 1L }
        (minTowelLength + 1..maxSize).forEach { length ->
            val newCache = buildCacheFixedLength(cache, towels, length, designs)
            if (length in minSize..maxSize) {
                finalCache += newCache
            }
            cache = newCache + cache.filterKeys { it.length + maxTowelLength >= length + 1 }
        }
        return finalCache
    }

    private fun buildCacheFixedLength(oldCache: Map<String, Long>, towels: List<String>, newLength: Int, designs: List<String>): Map<String, Long> {
        val cache = towels.filter { it.length == newLength }.associateWith { 1L }.toMutableMap()
        oldCache.forEach { (towel, count) ->
            towels.map { towel + it }
                .filter { it.length == newLength }
                .filter { designs.any { design -> design.startsWith(it) } }
                .forEach { cache.merge(it, count) { old, _ -> old + count } }
        }
        return cache
    }

    private fun isDesignPossibleCache(design: String, cache: Map<String, Long>, count: Long): Long {
        if (design.isEmpty()) return count
        return cache.filter { design.startsWith(it.key) }
            .map { isDesignPossibleCache(design.substring(it.key.length), cache, count * it.value) }
            .sum()
    }

    override fun puzzle2(input: Input): Long {
        val towels = input.values.first().split(",").map { it.trim() }
        val designs = input.values.drop(2).map { it.trim() }
        val cache = buildCache(towels, designs)
        return designs.sumOf { isDesignPossibleCache(it, cache, 1) }
    }
}