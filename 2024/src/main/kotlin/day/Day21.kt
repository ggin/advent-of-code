package day

import common.DailyChallenge
import common.Input
import common.Matrix
import common.Matrix.DirectionalValue
import common.Matrix.Value

typealias Moves = List<DirectionalValue<String>>
typealias KeypadMoves = Map<Pair<String, String>, Set<Moves>>

class Day21 : DailyChallenge {
    override fun puzzle1(input: Input): Long {
        val codes = input.values

        val numericKeypadMoves = numericKeypadMoves()

        val digitalKeypadMoves = digitalKeypadMoves()

        return codes.sumOf { code ->
            val firstCode = code.split("").filter { it.isNotEmpty() }
            val instructions = listOf("A") + firstCode
            val firstKeypadMoves = getMoves(numericKeypadMoves, instructions.joinToString("")).map { "A$it" }
            var moves = firstKeypadMoves
            (1..2).forEach { _ ->
                moves = moves.flatMap { getMoves(digitalKeypadMoves, it).map { "A$it" } }
                val minLength = moves.minBy { it.length }.length
                moves = moves.filter { it.length == minLength }
            }
            val minLength = moves.minBy { it.length }.length.toLong() - 1

            code.replace("A", "").toLong() * minLength
        }
    }

    private fun getMoves(keypad: KeypadMoves, instructions: String): List<String> {
        var paths: List<String> = listOf("")
        instructions.split("").filter { it.isNotEmpty() }.zipWithNext().map { pair ->
            val possibleMoves = keypad[pair]!!
            paths = if (possibleMoves.isEmpty()) paths.map { path -> path + "A" }
            else possibleMoves.flatMap { possibleMove -> paths.map { path -> path + possibleMove.drop(1).joinToString("") { it.direction.asString() } + "A" } }
        }
        return paths
    }

    private fun numericKeypadMoves(): KeypadMoves {
        val numericKeypad = Matrix(listOf(listOf("7", "8", "9"), listOf("4", "5", "6"), listOf("1", "2", "3"), listOf("#", "0", "A")))
        return numericKeypad.values().filter { it.value != "#" }
            .flatMap { startingButton ->
                numericKeypad.values().filter { it.value != "#" }/*.filter { it != startingButton }*/
                    .map { endingButton -> Pair(startingButton, endingButton) }
            }.associateWith { (startingButton, endingButton) -> findShortestPaths(numericKeypad, startingButton, endingButton) }
            .mapKeys { (pair, _) -> Pair(pair.first.value, pair.second.value) }
    }

    private fun digitalKeypadMoves(): KeypadMoves {
        val numericKeypad = Matrix(listOf(listOf("#", "^", "A"), listOf("<", "v", ">")))
        return numericKeypad.values().filter { it.value != "#" }
            .flatMap { startingButton ->
                numericKeypad.values().filter { it.value != "#" }
                    .map { endingButton -> Pair(startingButton, endingButton) }
            }.associateWith { (startingButton, endingButton) ->
                // a few overrides calculated by hand guaranteed to give shortest path in layer N - 2
                /*if (startingButton.value == "^" && endingButton.value == ">") setOf(DirectionalValue("^", Pair(0,1), Matrix.Direction.UP), DirectionalValue("A", Pair(0, 2), Matrix.Direction.RIGHT), DirectionalValue(">", Pair(1, 2),  x=0, y=2, value=A), Value(x=1, y=2, value=>))
                else */findShortestPaths(numericKeypad, startingButton, endingButton) }
            .mapKeys { (pair, _) -> Pair(pair.first.value, pair.second.value) }
    }

    private fun Matrix.Direction.asString() = when (this) {
        Matrix.Direction.UP -> "^"
        Matrix.Direction.DOWN -> "v"
        Matrix.Direction.LEFT -> "<"
        Matrix.Direction.RIGHT -> ">"
    }

    private fun findShortestPaths(
        matrix: Matrix<String>,
        startingButton: Value<String>,
        endingButton: Value<String>,
        visited: Moves = listOf(DirectionalValue(startingButton.value, startingButton.xy, Matrix.Direction.UP))
    ): Set<Moves> {
        val nextPositions = matrix.allValidDirections(startingButton).filter { it.value != "#" && !visited.contains(it) }

        nextPositions.find { it == endingButton }?.let { return setOf(visited + it) }

        val possibleMoves = nextPositions.filter { it != endingButton }.map { nextPosition ->
            findShortestPaths(matrix, nextPosition, endingButton, visited + nextPosition)
                // manually computed optimisations
                .asSequence()
                .filterNot{ it.size == 3 && it[0].value == "^" && it[1].value == "v" && it[2].value == ">" }
                .filterNot{ it.size == 4 && it[0].value == "A" && it[1].value == "^" && it[2].value == "v" && it[3].value == "<" }
                .filterNot{ it.size == 3 && it[0].value == "A" && it[1].value == "^" && it[2].value == "v" }
                .filterNot{ it.size == 4 && it[0].value == "<" && it[1].value == "v" && it[2].value == "^" && it[3].value == "A" }
                .filterNot{ it.size == 3 && it[0].value == ">" && it[1].value == "v" && it[2].value == "^" }
                // not sure about that one
                .filterNot{ it.size == 3 && it[0].value == "v" && it[1].value == ">" && it[2].value == "A" }
                .toList()

        }.flatten()

        val shortestPathLength = possibleMoves.minOfOrNull { it.size }
        return possibleMoves.filter { it.size == shortestPathLength }.toSet()
    }

    override fun puzzle2(input: Input): Long {
        val codes = input.values

        val numericKeypadMoves = numericKeypadMoves()

        val digitalKeypadMoves = digitalKeypadMoves()
        digitalKeypadMoves.filterValues { it.size > 1 }.forEach { println(it) }
        return codes.sumOf { code ->
            val firstCode = code.split("").filter { it.isNotEmpty() }
            val instructions = listOf("A") + firstCode
            val firstKeypadMoves = getMoves(numericKeypadMoves, instructions.joinToString("")).map { "A$it" }
            var moves = listOf("vA")
            (1..25).forEach { i ->
                moves = moves.flatMap { getMoves(digitalKeypadMoves, it).map { "A$it" } }
//                val minLength = moves.minBy { it.length }.length
//                moves = moves.filter { it.length == minLength }
                println("Iteration $i, moves ${moves.size}")
            }
            val minLength = moves.minBy { it.length }.length.toLong() - 1
            println("Min length $minLength")
            code.replace("A", "").toLong() * minLength
        }
    }
}