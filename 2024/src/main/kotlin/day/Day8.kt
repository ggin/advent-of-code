package day

import common.DailyChallenge
import common.Input
import common.Matrix
import common.toStringMatrix

class Day8 : DailyChallenge {
    override fun puzzle1(input: Input): Long {
        val matrix = input.toStringMatrix()
        val antennas = getAntennas(matrix)

        val antinodes = antennas.keys.flatMap { symbol ->
            antennas[symbol]!!.flatMap { a ->
                antennas[symbol]!!.filter { it.x != a.x && it.y != a.y }
                    .map { Pair(2 * a.x - it.x, 2 * a.y - it.y) }
                    .filter { matrix.contains(it) }
            }
        }

        return antinodes.distinct().count().toLong()
    }

    override fun puzzle2(input: Input): Long {
        val matrix = input.toStringMatrix()
        val antennas = getAntennas(matrix)

        val antinodes = antennas.keys.flatMap { symbol ->
            antennas[symbol]!!.flatMap { a ->
                antennas[symbol]!!.filter { it.x != a.x && it.y != a.y }
                    .flatMap {
                        val l: MutableList<Pair<Int, Int>> = mutableListOf(Pair(a.x, a.y))
                        var fact = 1
                        var newPair = Pair(a.x + fact * (a.x - it.x), a.y + fact * (a.y - it.y))
                        while (matrix.contains(newPair)) {
                            l += newPair
                            fact++
                            newPair = Pair(a.x + fact * (a.x - it.x), a.y + fact * (a.y - it.y))
                        }
                        l
                    }
            }
        }
        return antinodes.distinct().count().toLong()
    }

    private fun getAntennas(matrix: Matrix<String>): Map<String, List<Matrix.Value<String>>> {
        return matrix.values().groupBy { it.value }.filterKeys { it != "." }
    }

}