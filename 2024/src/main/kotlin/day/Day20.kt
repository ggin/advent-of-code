package day

import common.DailyChallenge
import common.Input
import common.Matrix
import common.Matrix.Value
import common.toStringMatrix

class Day20 : DailyChallenge {
    override fun puzzle1(input: Input): Long {
        val matrix = input.toStringMatrix()
        val start = matrix.findFirst("S")!!
        cheats.clear()

        val result = move(start, matrix, 2)!!
        listOf(2, 4, 6, 8, 10, 12, 20, 36, 38, 40, 64).forEach { savings ->
            println("There are ${cheats.filterValues { result - it == savings.toLong() }.size.toLong()} cheat that saves $savings picoseconds.")
        }
        return cheats.filterValues { result - it >= 100L }.size.toLong()
    }

    private var cheats = mutableMapOf<Set<Pair<Int, Int>>, Long>()

    private fun move(start: Value<String>, matrix: Matrix<String>, maxCheatCount: Int, visited: MutableSet<Value<String>> = mutableSetOf(start), cheatUsed: Boolean = false): Long? {
        var position = start
        while (position.value != "E") {
            val possibleNextMoves = matrix.allValidDirections(position).filter { !visited.contains(it) }
            if (!cheatUsed) {
                possibleNextMoves
                    .filter { it.value == "#" }
                    .forEach { cheat ->
                        nextLocationsAfterCheats(cheat, matrix, maxCheatCount, visited)
                            .filter { (positionAfterCheat, _) -> !visited.contains(positionAfterCheat) }
                            .forEach { (positionAfterCheat, visitedCheats) ->
                                if (positionAfterCheat.value == "E") {
                                    cheats.merge(setOf(position.xy, positionAfterCheat.xy), (visited + visitedCheats).size.toLong()) { a, b -> if (a < b) a else b }
                                } else if (positionAfterCheat.value == ".") {
                                    matrix.allValidDirections(positionAfterCheat).filter { !visited.contains(it) && it.value == "." }
                                        .forEach { nextMove ->
                                            val result = move(nextMove, matrix, maxCheatCount, (visited + visitedCheats + positionAfterCheat + nextMove).toMutableSet(), true)
                                            if (result != null) {
                                                cheats.merge(setOf(position.xy, positionAfterCheat.xy), result) { a, b -> if (a < b) a else b }
                                            }
                                        }
                                }
                            }
                    }
            }
            val nextPosition = possibleNextMoves.firstOrNull { it.value != "#" }
            if (nextPosition == null) return null
            position = nextPosition
            visited += position
        }
        return visited.size.toLong() - 1
    }

    private fun nextLocationsAfterCheats(
        initialCheat: Value<String>,
        matrix: Matrix<String>,
        maxCheatCount: Int,
        visitedPositions: Set<Value<String>>,
        visitedCheats: Set<Value<String>> = setOf(initialCheat),
        weightedCheats: MutableMap<Value<String>, Int> = mutableMapOf(Pair(initialCheat, 1))
    ): Map<Value<String>, Set<Value<String>>> {
        val nextPositions = matrix.allValidDirections(initialCheat).filter { !visitedPositions.contains(it) && !visitedCheats.contains(it) }
        val nextLocations: MutableMap<Value<String>, Set<Value<String>>> = nextPositions.filter { it.value == "." || it.value == "E" }.associateWith { visitedCheats }.toMutableMap()
        val newCheatCount = visitedCheats.size + 1
        if (newCheatCount < maxCheatCount) {
            nextPositions.filter { it.value == "#" || it.value == "." }
                .filter { weightedCheats[it] == null || weightedCheats[it]!! > newCheatCount }
                .forEach { cheat ->
                    weightedCheats[cheat] = newCheatCount
                    nextLocationsAfterCheats(cheat, matrix, maxCheatCount, visitedPositions, visitedCheats + cheat).forEach { (xy, count) ->
                        nextLocations.merge(xy, count) { a, b -> if (a.size < b.size) a else b }
                    }
                }
        }
        return nextLocations
    }

    override fun puzzle2(input: Input): Long {
        val matrix = input.toStringMatrix()
        val start = matrix.findFirst("S")!!
        cheats.clear()

        val result = move(start, matrix, 20)!!
        listOf(50, 52, 54, 56, 58, 60, 62, 64, 66, 68, 70, 72, 74, 76).forEach { savings ->
            println("There are ${cheats.filterValues { result - it == savings.toLong() }.size.toLong()} cheat that saves $savings picoseconds.")
        }
        cheats.filterValues { result - it == 76L }.forEach { (xy, count) ->
            println("Cheat at $xy saves $count picoseconds.")
        }
        return cheats.filterValues { result - it >= 100L }.size.toLong()
    }


}