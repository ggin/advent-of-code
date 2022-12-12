package day

import common.DailyChallenge
import common.Input


class Day12 : DailyChallenge {

    // somehow the unit tests pass but I'm off by 2 for both parts - values returned = 352, 351 - expected = 350, 349
    override fun puzzle1(input: Input): Long {
        val distances = calculateDistances(input)
        return distances.filterKeys { it.c == 'S' }.values.first() - 1L
    }

    override fun puzzle2(input: Input): Long {
        val distances = calculateDistances(input)
        return distances.filterKeys { it.c == 'S' || it.c == 'a' }.values.min() - 1L
    }


    data class Position(val x: Int, val y: Int, val c: Char)


    private fun move(
        mat: List<CharArray>,
        previousPosition: Position?,
        x: Int,
        y: Int,
        distances: MutableMap<Position, Int>
    ) {
        val currentPosition = Position(x, y, mat[x][y])
        val existingDistance = distances[currentPosition]
        val newDistance = (distances[previousPosition] ?: 0) + 1
        if (existingDistance == null || existingDistance > newDistance) {
            distances[currentPosition] = newDistance
            listOf(
                Pair(currentPosition.x + 1, currentPosition.y),
                Pair(currentPosition.x - 1, currentPosition.y),
                Pair(currentPosition.x, currentPosition.y + 1),
                Pair(currentPosition.x, currentPosition.y - 1),
            ).filter { it.first >= 0 && it.first < mat.size && it.second >= 0 && it.second < mat[0].size }
                .filter {
                    (mat[currentPosition.x][currentPosition.y] == 'E' && mat[it.first][it.second] == 'z')
                            || (mat[x][y] == 'a' && mat[it.first][it.second] == 'S')
                            || (mat[x][y] == 'b' && mat[it.first][it.second] == 'S')
                            || mat[currentPosition.x][currentPosition.y] == mat[it.first][it.second] + 1
                            || (mat[x][y] != 'E' && mat[it.first][it.second] != 'S' && mat[currentPosition.x][currentPosition.y] <= mat[it.first][it.second])
                }
                .forEach { nextPosition ->
                    move(mat, currentPosition, nextPosition.first, nextPosition.second, distances)
                }
        }
    }

    private fun calculateDistances(input: Input) : Map<Position, Int> {
        val mat = input.values.map { it.toCharArray() }
        val x = mat.indexOfFirst { it.contains('E') }
        val y = mat[x].indexOf('E')
        val distances = mutableMapOf<Position, Int>()
        move(mat, null, x, y, distances)
        return distances
    }
}
