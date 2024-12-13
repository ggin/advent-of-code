package day

import common.DailyChallenge
import common.Input

class Day13 : DailyChallenge {
    override fun puzzle1(input: Input) = solvePuzzle(input, { it }, { it <= 100 })
    override fun puzzle2(input: Input) = solvePuzzle(input, { it + 10000000000000L }, { it >= 100 })

    private fun solvePuzzle(input: Input, prizeConversion: (Long) -> Long, filter: (Long) -> Boolean): Long {
        val regex = "[+|=](\\d+)".toRegex()
        lateinit var A: Pair<Long, Long>
        lateinit var B: Pair<Long, Long>
        lateinit var prize: Pair<Long, Long>
        var tokens = 0L
        input.values.forEach {
            if (it.startsWith("Button A")) {
                val map = regex.findAll(it).map { m -> m.groupValues[1] }.toList()
                A = Pair(map[0].toLong(), map[1].toLong())
            } else if (it.startsWith("Button B")) {
                val map = regex.findAll(it).map { m -> m.groupValues[1] }.toList()
                B = Pair(map[0].toLong(), map[1].toLong())
            } else if (it.startsWith("Prize")) {
                val map = regex.findAll(it).map { m -> m.groupValues[1] }.toList()
                prize = Pair(prizeConversion(map[0].toLong()), prizeConversion(map[1].toLong()))
                tokens += minimumTokens(A, B, prize, filter)
            }
        }
        return tokens
    }

    private fun minimumTokens(a: Pair<Long, Long>, b: Pair<Long, Long>, p: Pair<Long, Long>, filter: (Long) -> Boolean): Long {
        val num = b.second * p.first - b.first * p.second
        val denum = b.second * a.first - b.first * a.second
        if (num % denum != 0L) return 0L
        val x = num / denum
        val y = (p.first - a.first * x) / b.first
        return if (filter(x) && filter(y)) x * 3 + y else 0L
    }
}