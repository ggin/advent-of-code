package day

import common.DailyChallenge
import common.Input
import common.Matrix
import common.toStringMatrix

class Day12 : DailyChallenge {
    override fun puzzle1(input: Input): Long {
        val matrix = input.toStringMatrix()
        val visited = mutableSetOf<Pair<Int, Int>>()
        var cost = 0L
        (0..<matrix.xCount).forEach { x ->
            (0..<matrix.yCount).forEach { y ->
                if (!visited.contains(Pair(x, y))) {
                    val visitedPlots = mutableSetOf<Pair<Int, Int>>()
                    val perimeter = findRegion(visitedPlots, matrix.at(x, y)!!.value, x, y, matrix)
                    cost += perimeter * visitedPlots.size
                    visitedPlots.forEach { visited += it }
                }
            }
        }
        return cost
    }

    private fun findRegion(visitedPlots: MutableSet<Pair<Int, Int>>, plant: String, x: Int, y: Int, m: Matrix<String>): Int {
        val newPlant = m.at(x, y)
        if (visitedPlots.contains(Pair(x, y))) return 0
        if (newPlant?.value != plant) return 1
        visitedPlots += Pair(x, y)
        return findRegion(visitedPlots, plant, x - 1, y, m) +
                findRegion(visitedPlots, plant, x + 1, y, m) +
                findRegion(visitedPlots, plant, x, y - 1, m) +
                findRegion(visitedPlots, plant, x, y + 1, m)
    }

    override fun puzzle2(input: Input): Long {
        val matrix = input.toStringMatrix()
        val visited = mutableSetOf<Pair<Int, Int>>()
        var cost = 0L
        (0..<matrix.xCount).forEach { x ->
            (0..<matrix.yCount).forEach { y ->
                if (!visited.contains(Pair(x, y))) {
                    val visitedPlots = mutableSetOf<Pair<Int, Int>>()
                    val perimeter = findRegionPart2(visitedPlots, matrix.at(x, y)!!.value, x, y, matrix)
                    cost += perimeter * visitedPlots.size
                    visitedPlots.forEach { visited += it }
                }
            }
        }
        return cost
    }

    private fun findRegionPart2(visitedPlots: MutableSet<Pair<Int, Int>>, plant: String, x: Int, y: Int, m: Matrix<String>): Int {
        val newPlant = m.at(x, y)
        if (visitedPlots.contains(Pair(x, y))) return 0
        if (newPlant?.value != plant) return 0
        visitedPlots += Pair(x, y)
        var fences = 0
        //top fence
        if (m.at(x-1, y)?.value != plant // no one on top
            && (m.at(x, y - 1)?.value != plant || m.at(x-1, y-1)?.value == plant)) fences += 1  // and no one left, or one top left

        // right fence
        if (m.at(x, y + 1)?.value != plant
            && (m.at(x - 1, y)?.value != plant || m.at(x - 1, y + 1)?.value == plant)) fences += 1

        // down fence
        if (m.at(x+1, y)?.value != plant
            && (m.at(x, y + 1)?.value != plant || m.at(x+1, y+1)?.value == plant)) fences += 1

        // left fence
        if (m.at(x, y - 1)?.value != plant
            && (m.at(x  + 1, y)?.value != plant || m.at(x + 1, y - 1)?.value == plant)) fences += 1

        return fences + findRegionPart2(visitedPlots, plant, x - 1, y, m) +
                findRegionPart2(visitedPlots, plant, x + 1, y, m) +
                findRegionPart2(visitedPlots, plant, x, y - 1, m) +
                findRegionPart2(visitedPlots, plant, x, y + 1, m)
    }
}