package day

import common.DailyChallenge
import common.Input

class Day18 : DailyChallenge {
    override fun puzzle1(input: Input): Long {
        val minX = 0
        val minY = 0
        val maxX = 70
        val maxY = 70
        val limit = 1024
        var position: Pair<Int, Int>? = Pair(minX, minY)
        val obstacles = input.toPairs(",") { it.toInt() }.take(limit)
        val visitedSpaces = mutableMapOf<Pair<Int, Int>, Long>()
        val unvisitedSpaces = (minX..maxX)
            .flatMap { x ->
                (minY..maxY)
                    .map { y -> Pair(x, y) }
                    .filter { !obstacles.contains(it) }
            }
            .associateWith { Long.MAX_VALUE }.toMutableMap()

        unvisitedSpaces[position!!] = 0L

        while (position != null) {
            listOf(
                Pair(position.first - 1, position.second),
                Pair(position.first + 1, position.second),
                Pair(position.first, position.second - 1),
                Pair(position.first, position.second + 1)
            ).filter { it.first in minX..maxX && it.second in minY..maxY }
                .filter { unvisitedSpaces.contains(it) }
                .forEach { newPosition ->
                    val newWeight = unvisitedSpaces[position]!! + 1
                    unvisitedSpaces.merge(newPosition, newWeight) { old, new -> if (old < new) old else new }
                }
            visitedSpaces += Pair(position, unvisitedSpaces[position]!!)
            unvisitedSpaces.remove(position)
            position = unvisitedSpaces.minByOrNull { it.value }?.key
        }

        return visitedSpaces[Pair(maxX, maxY)]!!
    }

    override fun puzzle2(input: Input): Long {
        val minX = 0
        val minY = 0
        val maxX = 70
        val maxY = 70
        val limit = 2911
        var position: Pair<Int, Int>? = Pair(minX, minY)
        val obstacles = input.toPairs(",") { it.toInt() }.take(limit)
        val visitedSpaces = mutableMapOf<Pair<Int, Int>, Long>()
        val unvisitedSpaces = (minX..maxX)
            .flatMap { x ->
                (minY..maxY)
                    .map { y -> Pair(x, y) }
                    .filter { !obstacles.contains(it) }
            }
            .associateWith { Long.MAX_VALUE }.toMutableMap()

        unvisitedSpaces[position!!] = 0L

        while (position != null) {
            listOf(
                Pair(position.first - 1, position.second),
                Pair(position.first + 1, position.second),
                Pair(position.first, position.second - 1),
                Pair(position.first, position.second + 1)
            ).filter { it.first in minX..maxX && it.second in minY..maxY }
                .filter { unvisitedSpaces.contains(it) }
                .forEach { newPosition ->
                    val newWeight = unvisitedSpaces[position]!! + 1
                    unvisitedSpaces.merge(newPosition, newWeight) { old, new -> if (old < new) old else new }
                }
            visitedSpaces += Pair(position, unvisitedSpaces[position]!!)
            unvisitedSpaces.remove(position)
            position = unvisitedSpaces.minByOrNull { it.value }?.key
        }

        return visitedSpaces[Pair(maxX, maxY)]!!
    }
}