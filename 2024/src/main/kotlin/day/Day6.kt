package day

import common.DailyChallenge
import common.Input

typealias MM = MutableList<MutableList<String>>
typealias PP = Triple</*x*/Int,/*y*/Int,/*direction*/Int>

class Day6 : DailyChallenge {

    private val directions = listOf("^", ">", "v", "<")

    override fun puzzle1(input: Input): Long {
        val map: MM = input.toStringList().map { it.toMutableList() }.toMutableList()
        var guard = map.findGuard()
        val positions: MutableSet<PP> = mutableSetOf()

        while (guard != null) {
            positions += guard
            guard = map.move(guard)
        }

        return positions.distinctBy { Pair(it.first, it.second) }.size.toLong()
    }

    private fun MM.findGuard(): PP? {
        return this.mapIndexedNotNull { x, l ->
            val y = l.mapIndexedNotNull { y, s -> if (s == "^") y else null }.firstOrNull()
            if (y != null) PP(x, y, 0) else null
        }.firstOrNull()
    }

    private fun MM.move(guard: PP): PP? {
        val newPos: PP
        val guardString = at(guard)
        newPos = nextPosition(guardString, guard)
        if (at(newPos) == "0") {
            this[guard.first][guard.second] = "."
            return null
        } else if (at(newPos) == ".") {
            this[guard.first][guard.second] = "."
            this[newPos.first][newPos.second] = guardString
            return newPos
        } else {
            val newDirection = (directions.indexOf(guardString) + 1) % 4
            this[guard.first][guard.second] = directions[newDirection]
            return PP(guard.first, guard.second, newDirection)
        }
    }

    private fun nextPosition(guardString: String, guard: PP) = when (guardString) {
        "^" -> {
            PP(guard.first - 1, guard.second, directions.indexOf(guardString))
        }

        ">" -> {
            PP(guard.first, guard.second + 1, directions.indexOf(guardString))
        }

        "v" -> {
            PP(guard.first + 1, guard.second, directions.indexOf(guardString))
        }

        else -> {
            PP(guard.first, guard.second - 1, directions.indexOf(guardString))
        }
    }

    private fun newObstruction(mm: MM, guard: PP, visited: List<PP>): PP? {
        val guardString = mm.at(guard)

        val newObstacle = nextPosition(guardString, guard)
        if (mm.at(newObstacle) != ".") return null
        if (visited.map { it.toPair() }.contains(newObstacle.toPair())) return null
        val newMM = mm.map { it.toMutableList() }.toMutableList()
        newMM[newObstacle.first][newObstacle.second] = "#"

        return if (valid(newMM, newObstacle, listOf(newObstacle))) newObstacle else null
    }

    private fun PP.toPair() = Pair(this.first, this.second)

    private fun valid(mm: MM, pp: PP, obstaclesEncountered: List<PP>): Boolean {
        val nextObstacle = nextObstruction(mm, pp) ?: return false
        if (obstaclesEncountered.contains(nextObstacle)) return true
        return valid(mm, nextObstacle, obstaclesEncountered + nextObstacle)
    }

    private fun nextObstruction(mm: MM, pp: PP) = functions[(pp.third + 1) % 4](mm, pp)

    private val functions: List<(MM, PP) -> PP?> = listOf(
        { m, p -> m.up(p) },
        { m, p -> m.right(p) },
        { m, p -> m.down(p) },
        { m, p -> m.left(p) })

    private fun MM.right(p: PP) = this[p.first + 1].withIndex().filter { it.index > p.second && it.value == "#" }.map { PP(p.first + 1, it.index, 1) }.firstOrNull()
    private fun MM.down(p: PP) = this.withIndex().filter { it.index > p.first && this[it.index][p.second - 1] == "#" }.map { PP(it.index, p.second - 1, 2) }.firstOrNull()
    private fun MM.left(p: PP) = this[p.first - 1].withIndex().filter { it.index < p.second && it.value == "#" }.map { PP(p.first - 1, it.index, 3) }.lastOrNull()
    private fun MM.up(p: PP) = this.withIndex().filter { it.index < p.first && this[it.index][p.second + 1] == "#" }.map { PP(it.index, p.second + 1, 0) }.lastOrNull()

    private fun MM.at(xy: PP): String {
        if (xy.first < 0 || xy.second < 0 || xy.first >= this.size || xy.second >= this[0].size) return "0"
        return this[xy.first][xy.second]
    }

    override fun puzzle2(input: Input): Long {
        val map: MM = input.toStringList().map { it.toMutableList() }.toMutableList()
        var guard = map.findGuard()
        val obstructions: MutableSet<PP> = mutableSetOf()
        val visited: MutableList<PP> = mutableListOf()
        while (guard != null) {
            guard.apply { visited += this }
            newObstruction(map, guard, visited)?.apply { obstructions += this }
            guard = map.move(guard)
        }

        return obstructions.distinctBy { Pair(it.first, it.second) }.size.toLong()
    }
}