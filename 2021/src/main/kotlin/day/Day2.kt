package day

import common.DailyChallenge

class Day2 : DailyChallenge {

    override fun puzzle1(values: List<String>): Long {
        var h: Long = 0
        var d: Long = 0
        values.map { s -> Pair(s.split(" ")[0], s.split(" ")[1].toLong()) }
            .forEach { (i, j) ->
                when (i) {
                    "forward" -> h += j
                    "down" -> d += j
                    "up" -> d -= j
                }
            }
        return h * d
    }

    override fun puzzle2(values: List<String>): Long {
        var h: Long = 0
        var d: Long = 0
        var a: Long = 0
        values.map { s -> Pair(s.split(" ")[0], s.split(" ")[1].toLong()) }
            .forEach { (i, j) ->
                when (i) {
                    "forward" -> {
                        h += j
                        d += a * j
                    }
                    "down" -> a += j
                    "up" -> a -= j
                }
            }
        return h * d
    }
}
