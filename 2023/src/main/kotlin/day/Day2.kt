package day

import common.DailyChallenge
import common.Input

class Day2 : DailyChallenge {

    private val regex = "Game (\\d+): ([a-z\\d\\s,;]+)".toRegex()
    private val blue = "(\\d+(?= blue))".toRegex()
    private val red = "(\\d+(?= red))".toRegex()
    private val green = "(\\d+(?= green))".toRegex()

    override fun puzzle1(input: Input): Long {
        return input.values.sumOf { line ->
            val group = regex.find(line)!!
            val index = group.groupValues[1].toLong()
            val ignore = group.groupValues[2].split(";")
                .map { it.trim() }
                .any {
                    val b = blue.find(it)?.groupValues?.get(1)?.toInt() ?: 0
                    val r = red.find(it)?.groupValues?.get(1)?.toInt() ?: 0
                    val g = green.find(it)?.groupValues?.get(1)?.toInt() ?: 0
                    r > 12 || g > 13 || b > 14
                }
            if (ignore) 0L else index
        }

    }

    override fun puzzle2(input: Input): Long {
        return input.values.sumOf { line ->
            val group = regex.find(line)!!
            var maxB = 1L
            var maxR = 1L
            var maxG = 1L
            group.groupValues[2].split(";")
                .map { it.trim() }
                .forEach {
                    val b = blue.find(it)?.groupValues?.get(1)?.toLong() ?: 0L
                    if (b > maxB) maxB = b
                    val r = red.find(it)?.groupValues?.get(1)?.toLong() ?: 0L
                    if (r > maxR) maxR = r
                    val g = green.find(it)?.groupValues?.get(1)?.toLong() ?: 0L
                    if (g > maxG) maxG = g
                }
            maxB * maxR * maxG
        }

    }

}
