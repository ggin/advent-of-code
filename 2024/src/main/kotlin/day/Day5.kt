package day


import common.DailyChallenge
import common.Input

typealias Predicate = (List<Int>) -> Boolean

class Day5 : DailyChallenge {
    override fun puzzle1(input: Input): Long {
        val predicates = parsePredicates(input)

        return parseUpdates(input)
            .filter { l -> predicates.all { it(l) } }
            .sumOf { l -> l.elementAt(l.size / 2) }
            .toLong()
    }

    override fun puzzle2(input: Input): Long {
        val predicates = parsePredicates(input)
        return parseUpdates(input).asSequence()
            .filter { l -> predicates.any { it(l).not() } }
            .map { l -> l.sortedWith { x, y -> if (predicates.all { it(listOf(x, y)) }) 1 else -1 } }
            .sumOf { l -> l.elementAt(l.size / 2) }
            .toLong()
    }

    private fun parsePredicates(input: Input): List<Predicate> {
        val text = input.content.split("\n\n")
        val predicates: List<Predicate> = text[0].split("\n").map {
            val x = it.split("|")[0].toInt()
            val y = it.split("|")[1].toInt()
            val p: Predicate = { l -> l.indexOf(x) == -1 || l.indexOf(y) == -1 || l.indexOf(x) <= l.indexOf(y) }
            p
        }
        return predicates
    }

    private fun parseUpdates(input: Input) = input.content.split("\n\n")[1]
        .split("\n")
        .map { it.split(",").map { it.toInt() } }

}