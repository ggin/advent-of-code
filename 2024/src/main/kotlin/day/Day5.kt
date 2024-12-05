package day


import common.DailyChallenge
import common.Input

class Predicate(val function: (List<Int>) -> Boolean, val x: Int, val y: Int) : (List<Int>) -> Boolean {
    override fun invoke(p1: List<Int>): Boolean {
        return function(p1)
    }
}

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
            .map { l ->
                var ordered = false
                var ll = l
                while (!ordered) {
                    ll = predicates.find { it(ll).not() }?.let { swapValues(ll, it.x, it.y) } ?: ll.apply { ordered = true }
                }
                ll
            }
            .sumOf { l -> l.elementAt(l.size / 2) }
            .toLong()
    }

    private fun parsePredicates(input: Input): List<Predicate> {
        val text = input.content.split("\n\n")
        val predicates: List<Predicate> = text[0].split("\n").map {
            val x = it.split("|")[0].toInt()
            val y = it.split("|")[1].toInt()
            Predicate({ l -> l.indexOf(x) == -1 || l.indexOf(y) == -1 || l.indexOf(x) <= l.indexOf(y) }, x, y)
        }
        return predicates
    }

    private fun parseUpdates(input: Input) = input.content.split("\n\n")[1]
        .split("\n")
        .map { it.split(",").map { it.toInt() } }

    private fun swapValues(l: List<Int>, x: Int, y: Int): List<Int> {
        val indexX = l.indexOf(x)
        val indexY = l.indexOf(y)
        val result = l.toMutableList()
        result[indexX] = y
        result[indexY] = x
        return result
    }
}