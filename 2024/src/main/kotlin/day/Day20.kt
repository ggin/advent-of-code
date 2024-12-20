package day

import common.DailyChallenge
import common.Input
import common.Matrix
import common.Matrix.Value
import common.toStringMatrix
import kotlin.math.abs

class Day20 : DailyChallenge {
    override fun puzzle1(input: Input): Long {
        val matrix = input.toStringMatrix()
        val start = matrix.findFirst("S")!!

        val createDistanceMap = createDistanceMap(start, matrix)
        val results = createDistanceMap.keys
            .map { shortcuts(it, createDistanceMap, 2, 100) }
            .flatten()
        return results.size.toLong()
    }

    private fun createDistanceMap(start: Value<String>, matrix: Matrix<String>): Map<Value<String>, Int> {
        var position = start
        val distanceMap = mutableMapOf(Pair(position, 0))
        val visited = mutableSetOf(start)
        while (position.value != "E") {
            position = matrix.allValidDirections(position).filter { !visited.contains(it) }.first { it.value != "#" }
            visited += position
            distanceMap[position] = visited.size - 1
        }
        return distanceMap
    }

    private fun shortcuts(start: Value<String>, distance: Map<Value<String>, Int>, cheatDuration: Int, threshold: Int): Set<Value<String>> {
        val originalDistance = distance[start]!!
        return distance
            .filter { (v, _) -> abs(v.x - start.x) + abs(v.y - start.y) in 1 .. cheatDuration }
            .filter { (v, d) -> d - originalDistance - abs(v.x - start.x) - abs(v.y - start.y) >= threshold }
            .keys
    }

    override fun puzzle2(input: Input): Long {
        val matrix = input.toStringMatrix()
        val start = matrix.findFirst("S")!!

        val createDistanceMap = createDistanceMap(start, matrix)
        val results = createDistanceMap.keys
            .map { shortcuts(it, createDistanceMap, 20, 100) }
            .flatten()
        return results.size.toLong()
    }

}