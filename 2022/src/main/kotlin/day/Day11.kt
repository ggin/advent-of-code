package day

import common.DailyChallenge
import common.Input


class Day11 : DailyChallenge {

    override fun puzzle1(input: Input) = solve(input, 20, 3)

    override fun puzzle2(input: Input) = solve(input, 10000, 1)

    private fun solve(input: Input, nbRounds: Int, reliefFactor: Int) : Long {
        val monkeys = parseInput(input)
        val modulo = monkeys.map { it.divisibleBy }.fold(1L) { acc, x -> acc * x}
        (1..nbRounds).forEach { _ ->
            monkeys.forEach { monkey ->
                monkey.items.forEach { item ->
                    monkey.nbItemsInspected++
                    val newItem = monkey.operation(item) / reliefFactor
                    if (newItem.mod(monkey.divisibleBy) == 0) {
                        monkeys[monkey.destIfTrue].items += newItem.mod(modulo)
                    } else {
                        monkeys[monkey.destIfFalse].items += newItem.mod(modulo)
                    }
                }
                monkey.items.clear()

            }
        }
        return monkeys.map { it.nbItemsInspected }.sortedDescending().take(2).fold(1L) { acc: Long, i: Long -> acc * i }
    }

    private val regex =
        "Monkey (\\d+):\n.*:([\\d,\\s]+)\n.*old (.*)\n.*divisible by (\\d+)\n.* monkey (\\d+)\n.* monkey (\\d+)".toRegex()

    //".*Starting items: (\\d+):".toRegex()
    private fun parseInput(input: Input) = regex.findAll(input.content).map { toMonkey(it) }.toList()

    private fun toMonkey(match: MatchResult): Monkey {
        return Monkey(
            match.groupValues[1].toInt(),
            match.groupValues[2].split(",").map { it.trim().toLong() }.toMutableList(),
            match.groupValues[3].split(" ").let {
                { x: Long ->
                    val y = if (it[1] == "old") x else it[1].toLong()
                    if (it[0] == "+") x + y else x * y
                }
            },
            match.groupValues[4].toInt(),
            match.groupValues[5].toInt(),
            match.groupValues[6].toInt()
        )
    }

    private data class Monkey(
        val id: Int,
        val items: MutableList<Long>,
        val operation: (Long) -> Long,
        val divisibleBy: Int,
        val destIfTrue: Int,
        val destIfFalse: Int,
        var nbItemsInspected: Long = 0L
    )
}
