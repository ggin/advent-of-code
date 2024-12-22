package day

import common.DailyChallenge
import common.Input
import java.util.concurrent.atomic.AtomicInteger

class Day22 : DailyChallenge {
    override fun puzzle1(input: Input): Long {
        return input.values.map { it.toLong() }.sumOf { buyerSecret ->
            (1..2000).fold(buyerSecret) { secret, _ -> nextSecretNumber(secret) }
        }
    }

    private fun nextSecretNumber(secret: Long): Long {
        var result = ((secret * 64) xor secret).mod(16777216L)
        result = ((result / 32) xor result).mod(16777216L)
        result = ((result * 2048) xor result).mod(16777216L)
        return result
    }

    override fun puzzle2(input: Input): Long {
        val possibleSequences = mutableSetOf<List<Int>>()
        val pricesAndChanges = input.values.map { it.toLong() }.map { buyerSecret ->
            val secrets = mutableListOf<Int>()
            (1..2000).fold(buyerSecret) { secret, _ -> nextSecretNumber(secret).also { secrets += it.mod(10) } }
            val priceChanges = (listOf(buyerSecret.mod(10)) + secrets).zipWithNext().map { (a, b) -> b - a }
            possibleSequences += (secrets zip priceChanges).windowed(4).filter { it.last().first == 9 }.map { l -> l.map { it.second } }
            secrets zip priceChanges
        }
        println("Found ${possibleSequences.size} possible sequences")
        val i = AtomicInteger(0)
        return possibleSequences.parallelStream().map { sequence ->
            if (i.getAndIncrement() % 100 == 0) println("Processing item $i")
            pricesAndChanges.sumOf { buyer ->
                buyer.windowed(4).firstOrNull { sequence == (it.map { l -> l.second }) }?.map { it.first }?.last()?.toLong() ?: 0L
            }
        }.max(Long::compareTo).get()
    }
}