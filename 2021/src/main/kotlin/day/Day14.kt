package day

import common.DailyChallenge

class Day14 : DailyChallenge {

    override fun puzzle1(values: List<String>) = puzzleInternal(values, 10)

    override fun puzzle2(values: List<String>) = puzzleInternal(values, 40)

    private fun puzzleInternal(values: List<String>, iterations: Int): Long {
        val template = parseTemplate(values)
        val rules = parseRules(values)
        val charOccurrences = (1..iterations)
            .fold(groupPairOccurrences(template)) { acc, _ -> countPairOccurrences(rules, acc) }
            .let { toCharOccurrences(it) }
        return charOccurrences.maxOrNull()!! - charOccurrences.minOrNull()!!.toLong()
    }

    private fun parseTemplate(values: List<String>) = values.first()

    private fun parseRules(values: List<String>) = values
        .drop(2)
        .map { it.split("->") }.associate { Pair(it[0].trim(), it[1].trim()) }

    private fun groupPairOccurrences(template: String) =
        (0 until template.length - 1).map { template.slice(it..it + 1) }
            .groupingBy { it }
            .eachCount()
            .mapValues { (_, v) -> v.toLong() } + (template.last().toString() to 1L)

    private fun countPairOccurrences(
        rules: Map<String, String>,
        occurrences: Map<String, Long>
    ): Map<String, Long> {
        val m: MutableMap<String, Long> = mutableMapOf()
        occurrences.filterKeys { it.length == 2 }.forEach { (k, v) ->
            val newLetter = rules[k]!!
            m.incrementBy(k[0] + newLetter, v)
            m.incrementBy(newLetter + k[1], v)
        }
        occurrences.filterKeys { it.length == 1 }.forEach { (k, _) -> m.incrementBy(k, 1) }
        return m
    }

    private fun toCharOccurrences(pairOccurrences: Map<String, Long>) =
        pairOccurrences.keys.map { it.toCharArray().map { c -> c.toString() } }.flatten()
            .map { c -> pairOccurrences.filterKeys { k -> k.startsWith(c) }.values.sum() }

    private fun MutableMap<String, Long>.incrementBy(k: String, value: Long) =
        this.compute(k) { _, v -> if (v == null) value else v + value }

}
