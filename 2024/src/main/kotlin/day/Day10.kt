package day

import common.DailyChallenge
import common.Input
import common.Matrix
import common.Matrix.Value
import common.toIntMatrix

class Day10 : DailyChallenge {
    override fun puzzle1(input: Input): Long {
        val map = input.toIntMatrix()
        return map.findAll(0).sumOf { trailhead ->
            map.reachableHeights(trailhead).distinct().size.toLong()
        }
    }

    override fun puzzle2(input: Input): Long {
        val map = input.toIntMatrix()
        return map.findAll(0).sumOf { trailhead ->
            map.reachableHeights(trailhead).count().toLong()
        }
    }


    private fun Matrix<Int>.reachableHeights(pos: Value<Int>?, visitedPaths: List<Int> = listOf()): List<Value<Int>> {
        if (pos == null) return listOf()
        val newVisited = visitedPaths + pos.value
        if ((0..pos.value).toList() != newVisited) return listOf()
        if (pos.value == 9) return listOf(pos)
        return reachableHeights(this.at(pos.x, pos.y - 1), newVisited) +
                reachableHeights(this.at(pos.x, pos.y + 1), newVisited) +
                reachableHeights(this.at(pos.x - 1, pos.y), newVisited) +
                reachableHeights(this.at(pos.x + 1, pos.y), newVisited)
    }

}