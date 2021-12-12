package day

import common.DailyChallenge

typealias Paths = Map<String, List<String>>

class Day12 : DailyChallenge {

    override fun puzzle1(values: List<String>): Long {
        val paths = buildPaths(values)
        return traversePath("start", paths, listOf(), 1)
    }

    override fun puzzle2(values: List<String>): Long {
        val paths = buildPaths(values)
        return traversePath("start", paths, listOf(), 2)
    }

    private fun buildPaths(values: List<String>): Paths {
        return values
            .asSequence()
            .map { it.split("-") }
            .map { listOf(it, it.reversed()) }
            .flatten()
            .filter { it[1] != "start" }
            .groupBy({ it[0] }, { it[1] })
    }

    private fun traversePath(
        start: String,
        paths: Paths,
        smallCavesVisited: List<String>,
        smallCaveVisitAllowed: Int
    ): Long {
        if (start == "end") {
            return 1L
        }
        if (!paths.containsKey(start)) {
            return 0L
        }
        if (!start.isUpperCase() && smallCavesVisited.contains(start) && smallCaveVisitAllowed <= 1) {
            return 0L
        }
        val newSmallCasesVisited =
            if (start.isUpperCase()) smallCavesVisited else smallCavesVisited + start
        val newSmallCaseVisitAllowed =
            if (!smallCavesVisited.contains(start)) smallCaveVisitAllowed else smallCaveVisitAllowed - 1

        return paths[start]!!.sumOf {
            if (it.isUpperCase()) {
                traversePath(it, paths, newSmallCasesVisited, newSmallCaseVisitAllowed)
            } else {
                traversePath(it, paths, newSmallCasesVisited, newSmallCaseVisitAllowed)
            }
        }
    }

    private fun String.isUpperCase() = this.uppercase() == this

}

