package day

import common.DailyChallenge
import common.Input

class Day6 : DailyChallenge {

    data class Race(val time: Long, val distance: Long) {

    }

    private fun parse(text: String): List<Race> {
        val times = text.lines().first().split(":")[1].split(" ").filter { it.isNotEmpty() }.map { it.toLong() }
        val distances = text.lines().last().split(":")[1].split(" ").filter { it.isNotEmpty() }.map { it.toLong() }
        return times.indices.map { Race(times[it], distances[it]) }
    }

    private fun parseSingleRace(text: String): Race {
        val time = text.lines().first().split(":")[1].split(" ").filter{ it.isNotEmpty() }.joinToString("").toLong()
        val distance = text.lines().last().split(":")[1].split(" ").filter{ it.isNotEmpty() }.joinToString("").toLong()
        return Race(time, distance)
    }

    private fun waysToWin(race: Race): Int {
        var holdFor = 1
        var count = 0
        while (holdFor < race.time) {
            if (holdFor * (race.time - holdFor) > race.distance) count++
            holdFor++
        }
        return count
    }

    override fun puzzle1(input: Input) = parse(input.content).fold(1L) { acc, next -> acc * waysToWin(next) }

    override fun puzzle2(input: Input) = waysToWin(parseSingleRace(input.content)).toLong()

    private fun solve(s: String, windowSize: Int) = s.windowedSequence(windowSize, 1)
        .map { it.toSet().size == it.length }
        .indexOfFirst { it } + windowSize

}
