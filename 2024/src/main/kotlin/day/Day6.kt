package day

import common.DailyChallenge
import common.Input

typealias Grid = List<List<String>>
typealias Coordinates = Pair<Int, Int>

data class Position(val x: Int, val y: Int, val direction: Int) {
    fun coordinates(): Coordinates = Pair(x, y)
    fun rotate() = Position(x, y, (direction + 1) % 4)
    fun advance() = when (direction) {
        0 -> Position(x - 1, y, direction)
        1 -> Position(x, y + 1, direction)
        2 -> Position(x + 1, y, direction)
        else -> Position(x, y - 1, direction)
    }
}

class Day6 : DailyChallenge {

    override fun puzzle1(input: Input): Long {
        val map: Grid = input.toStringList().map { it.toMutableList() }.toMutableList()
        var guard = map.findGuard()
        val positions: MutableSet<Position> = mutableSetOf()

        while (guard != null) {
            positions += guard
            guard = map.move(guard)
        }

        return positions.distinctBy { it.coordinates() }.size.toLong()
    }

    private fun Grid.findGuard(): Position? {
        return this.mapIndexedNotNull { x, l ->
            val y = l.mapIndexedNotNull { y, s -> if (s == "^") y else null }.firstOrNull()
            if (y != null) Position(x, y, 0) else null
        }.firstOrNull()
    }

    private fun Grid.move(guard: Position): Position? {
        val newPos: Position = guard.advance()
        return if (at(newPos) == "0") null
        else if (at(newPos) == "#") guard.rotate()
        else newPos
    }

    private fun newObstruction(grid: Grid, guard: Position, visited: List<Coordinates>): Position? {
        val newObstacle = guard.advance()
        if (grid.at(newObstacle) != ".") return null
        if (visited.contains(newObstacle.coordinates())) return null
        val newMM = grid.map { it.toMutableList() }.toMutableList()
        newMM[newObstacle.x][newObstacle.y] = "#"

        return if (doesObstacleCauseLoop(newMM, newObstacle, listOf(newObstacle))) newObstacle else null
    }

    private fun doesObstacleCauseLoop(grid: Grid, position: Position, obstaclesEncountered: List<Position>): Boolean {
        val nextObstacle = findNextObstacle(grid, position) ?: return false
        if (obstaclesEncountered.contains(nextObstacle)) return true // loop detected
        return doesObstacleCauseLoop(grid, nextObstacle, obstaclesEncountered + nextObstacle)
    }

    private fun findNextObstacle(grid: Grid, position: Position) = findNextObstacleFunctions[(position.direction + 1) % 4](grid, position)

    private val findNextObstacleFunctions: List<(Grid, Position) -> Position?> = listOf(
        { m, p -> m.nextObstacleUp(p) },
        { m, p -> m.nextObstacleOnTheRight(p) },
        { m, p -> m.nextObstacleDown(p) },
        { m, p -> m.nextObstacleOnTheLeft(p) })

    private fun Grid.nextObstacleOnTheRight(p: Position) = this[p.x + 1].withIndex().filter { it.index > p.y && it.value == "#" }.map { Position(p.x + 1, it.index, 1) }.firstOrNull()
    private fun Grid.nextObstacleDown(p: Position) = this.withIndex().filter { it.index > p.x && this[it.index][p.y - 1] == "#" }.map { Position(it.index, p.y - 1, 2) }.firstOrNull()
    private fun Grid.nextObstacleOnTheLeft(p: Position) = this[p.x - 1].withIndex().filter { it.index < p.y && it.value == "#" }.map { Position(p.x - 1, it.index, 3) }.lastOrNull()
    private fun Grid.nextObstacleUp(p: Position) = this.withIndex().filter { it.index < p.x && this[it.index][p.y + 1] == "#" }.map { Position(it.index, p.y + 1, 0) }.lastOrNull()

    private fun Grid.at(xy: Position): String {
        if (xy.x < 0 || xy.y < 0 || xy.x >= this.size || xy.y >= this[0].size) return "0"
        return this[xy.x][xy.y]
    }

    override fun puzzle2(input: Input): Long {
        val map: Grid = input.toStringList().map { it.toMutableList() }.toMutableList()
        var guard = map.findGuard()
        val obstructions: MutableSet<Position> = mutableSetOf()
        val visited: MutableList<Coordinates> = mutableListOf()
        while (guard != null) {
            guard.apply { visited += this.coordinates() }
            newObstruction(map, guard, visited)?.apply { obstructions += this }
            guard = map.move(guard)
        }

        return obstructions.distinctBy { it.coordinates() }.size.toLong()
    }
}