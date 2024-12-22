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
                else */findShortestPaths(numericKeypad, startingButton, endingButton)
            }
            .mapKeys { (pair, _) -> Pair(pair.first.value, pair.second.value) }
    }

    private fun digitalKeypadMoves2(): Map<Pair<String, String>, String> {
        val numericKeypad = Matrix(listOf(listOf("#", "^", "A"), listOf("<", "v", ">")))
        return numericKeypad.values().filter { it.value != "#" }
            .flatMap { startingButton ->
                numericKeypad.values().filter { it.value != "#" }
                    .map { endingButton -> Pair(startingButton, endingButton) }
            }.associateWith { (startingButton, endingButton) ->
                // a few overrides calculated by hand guaranteed to give shortest path in layer N - 2
                /*if (startingButton.value == "^" && endingButton.value == ">") setOf(DirectionalValue("^", Pair(0,1), Matrix.Direction.UP), DirectionalValue("A", Pair(0, 2), Matrix.Direction.RIGHT), DirectionalValue(">", Pair(1, 2),  x=0, y=2, value=A), Value(x=1, y=2, value=>))
                else */findShortestPaths(numericKeypad, startingButton, endingButton).firstOrNull()?.drop(1)?.joinToString("") { it.direction.asString() } ?: ""
            }
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
//                .filterNot { it.size == 3 && it[0].value == "^" && it[1].value == "v" && it[2].value == ">" }
//                .filterNot { it.size == 4 && it[0].value == "A" && it[1].value == "^" && it[2].value == "v" && it[3].value == "<" }
//                .filterNot { it.size == 3 && it[0].value == "A" && it[1].value == ">" && it[2].value == "v" }
//                .filterNot { it.size == 4 && it[0].value == "<" && it[1].value == "v" && it[2].value == "^" && it[3].value == "A" }
//                .filterNot { it.size == 3 && it[0].value == ">" && it[1].value == "A" && it[2].value == "^" }
                // not sure about that one
//                .filterNot { it.size == 3 && it[0].value == "v" && it[1].value == "^" && it[2].value == "A" }

//                .filterNot { it.size == 4 && it[0].value == "2" && it[1].value == "5" && it[2].value == "8" && it[3].value == "9" }
//                .filterNot { it.size == 4 && it[0].value == "2" && it[1].value == "5" && it[2].value == "6" && it[3].value == "9" }
                .toList()

        }.flatten()

        val shortestPathLength = possibleMoves.minOfOrNull { it.size }
        return possibleMoves.filter { it.size == shortestPathLength }.toSet()
    }

    override fun puzzle2(input: Input): Long {
        val codes = input.values

        val numericKeypadMoves = numericKeypadMoves()
        val digitalKeypadMoves = digitalKeypadMoves()
        return codes.sumOf { code ->
            val firstCode = code.split("").filter { it.isNotEmpty() }
            val instructions = listOf("A") + firstCode
            val listOfNextMoves = getMoves(numericKeypadMoves, instructions.joinToString(""))
            var moves = listOfNextMoves.map { mapOf(Pair(it, 1)) }
            (1..25).forEach { _ ->
                val newMoves = moves.flatMap { getMoves3(digitalKeypadMoves, it) }
                val minLength = newMoves.map { map -> map.minBy { it.value * it.key.length.toLong() } }
                moves = newMoves.filter { m -> m.map { it.value * it.key.length.toLong() }.sum() == minLength }
            }
            code.replace("A", "").toLong() * moves.first().map { it.value.toLong() * it.key.length }.sum()
        }
    }
    // 162732645146414 too low


    private fun getMoves2(keypad: KeypadMoves, instructions: Map<String, Int>): Map<String, Int> {
        val moves = mutableMapOf<String, Int>()
        instructions.forEach { (instruction, count) ->
            val newInstruction = "A$instruction"
            var key = ""
            newInstruction.zipWithNext()
                .map { Triple(it.first, it.second, keypad[Pair(it.first.toString(), it.second.toString())]!!) }
                .forEach {
                    val paths = it.third.map { l ->
                        l.drop(1).joinToString("") { dv -> dv.direction.asString() }
                    } + "A"


//                val path: String = it.third.map { it.drop(1).joinToString("") { it.direction.asString() } }
                    val path = paths.firstOrNull() ?: ""
                    key += path + "A"
                    if (it.second == 'A') {
                        moves.merge(key, count, Int::plus)
                        key = ""
                    }
                }
        }
        // v<<A>>^A <A>A vA<^AA>A <vAAA>^A

        // v<<A >>^A    <A  >A  vA  <^A A   >A  <vA A   A   >^A
        // <    A       ^   A   >   ^   ^   A   v   v   v   A
        return moves
    }

    private fun getMoves3(keypad: KeypadMoves, instructions: Map<String, Int>): List<Map<String, Int>> {
        val moves = mutableMapOf<String, Int>()
        var list = mutableListOf(moves)
        instructions.forEach { (instruction, count) ->
            val newInstruction = "A$instruction"
            var keys = mutableListOf("")
            newInstruction.zipWithNext()
                .map { Triple(it.first, it.second, keypad[Pair(it.first.toString(), it.second.toString())]!!) }
                .forEach {
                    val paths = it.third.map { l ->
                        l.drop(1).joinToString("") { dv -> dv.direction.asString() }
                    } + "A"

                    if (paths.size > 1) {
                        list = list.flatMap { map -> (1..paths.size).map { map.toMutableMap() } }.toMutableList()
                        keys = keys.flatMap { key -> (1..paths.size).map { path -> key + path + "A" } }.toMutableList()
                    } else {
//                val path: String = it.third.map { it.drop(1).joinToString("") { it.direction.asString() } }
                        val path = paths.firstOrNull() ?: ""
                        keys = keys.map { it + path + "A" }.toMutableList()
                    }
                    if (it.second == 'A') {
                        (list zip keys).forEach { (move, key) -> move.merge(key, count, Int::plus) }
                        keys = keys.map { "" }.toMutableList()

                    }
                }
        }
        // v<<A>>^A <A>A vA<^AA>A <vAAA>^A

        // v<<A >>^A    <A  >A  vA  <^A A   >A  <vA A   A   >^A
        // <    A       ^   A   >   ^   ^   A   v   v   v   A
        return list
    }


}