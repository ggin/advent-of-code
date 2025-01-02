package day

import common.DailyChallenge
import common.Input
import common.Matrix
import common.Matrix.DirectionalValue
import common.Matrix.Value
import common.cartesianProduct

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
            (1..2).forEach { iter ->
                moves = moves.flatMap { getMoves(digitalKeypadMoves, it).map { "A$it" } }
                val minLength = moves.minBy { it.length }.length
                moves = moves.filter { it.length == minLength }
//                println("Iteration ${iter} Min length: ${minLength}")
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
            else possibleMoves.flatMap { possibleMove ->
                paths.map { path ->
                    path + possibleMove.drop(1).joinToString("") { it.direction.asString() } + "A"
                }
            }
        }
        return paths
    }

    private fun numericKeypadMoves(): KeypadMoves {
        val numericKeypad =
            Matrix(listOf(listOf("7", "8", "9"), listOf("4", "5", "6"), listOf("1", "2", "3"), listOf("#", "0", "A")))
        return numericKeypad.values().filter { it.value != "#" }
            .flatMap { startingButton ->
                numericKeypad.values().filter { it.value != "#" }/*.filter { it != startingButton }*/
                    .map { endingButton -> Pair(startingButton, endingButton) }
            }.associateWith { (startingButton, endingButton) ->
                findShortestPaths(
                    numericKeypad,
                    startingButton,
                    endingButton
                )
            }
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
        val nextPositions =
            matrix.allValidDirections(startingButton).filter { it.value != "#" && !visited.contains(it) }

        nextPositions.find { it == endingButton }?.let { return setOf(visited + it) }

        val possibleMoves = nextPositions.filter { it != endingButton }.map { nextPosition ->
            findShortestPaths(matrix, nextPosition, endingButton, visited + nextPosition)
                // manually computed optimisations
                .asSequence()
                .filterNot { it.size == 3 && it[0].value == "^" && it[1].value == "v" && it[2].value == ">" }
                .filterNot { it.size == 4 && it[0].value == "A" && it[1].value == "^" && it[2].value == "v" && it[3].value == "<" }
                .filterNot { it.size == 3 && it[0].value == "A" && it[1].value == "^" && it[2].value == "v" }
                .filterNot { it.size == 4 && it[0].value == "<" && it[1].value == "v" && it[2].value == "^" && it[3].value == "A" }
                .filterNot { it.size == 3 && it[0].value == ">" && it[1].value == "v" && it[2].value == "^" }
                // not sure about that one
                .filterNot { it.size == 3 && it[0].value == "v" && it[1].value == ">" && it[2].value == "A" }
                .toList()

        }.flatten()

        val shortestPathLength = possibleMoves.minOfOrNull { it.size }
        return possibleMoves.filter { it.size == shortestPathLength }.toSet()
    }

    private fun nextIteration(m: Map<String, Int>): List<Map<String, Int>> {
        val newMap = mutableMapOf<String, Int>()
        m.forEach { (s, count) ->
            s.split("A").dropLast(1).map { "A${it}A" }.forEach { newKey ->
                newMap.merge(newKey, count, Int::plus)
            }
        }
        val nextGroups = newMap.map { (group, count) ->
            val possibleNextInstructions =
                group.zipWithNext().map { directionalMappings[it]!!.map { s -> "${s}A" } }
            (cartesianProduct(possibleNextInstructions) { it.reduce { acc, s -> acc + s } }).map {
                Pair(
                    it,
                    count
                )
            }
        }
        return buildMaps(nextGroups)
    }

    private fun instructionsForNextRobot(currentInstructions: String): List<String> {
        val groups = currentInstructions.split("A").dropLast(1)
        val nextGroups = groups.map { group ->
            val groupInstructions = "A${group}A"
            val possibleNextInstructions =
                groupInstructions.zipWithNext().map { directionalMappings[it]!!.map { s -> "${s}A" } }
            cartesianProduct(possibleNextInstructions) { it.reduce { acc, s -> acc + s } }
        }
        return cartesianProduct(nextGroups) { it.reduce { acc, s -> acc + s } }.toList()
    }

    private fun nextInstructionsForNextRobot(currentInstructions: Instructions): List<Instructions> {
        val nextGroups = currentInstructions.groups().map { group ->
            val groupInstructions = "A${group.first}"
            val possibleNextInstructions = groupInstructions.zipWithNext()
                .map { directionalMappings[it]!!.map { s -> Instructions.of("${s}A", group.second) } }

            cartesianProduct(possibleNextInstructions) { it.reduce { acc, i -> acc.merge(i) } }
        }

        return cartesianProduct(nextGroups) { it.reduce { acc, i -> acc.merge(i) } }.toList()
    }

    data class PreviousInstructions(val previousInstructions: List<Instructions>) {
        fun add(instruction: Instructions): PreviousInstructions {
            return PreviousInstructions(previousInstructions + instruction)
        }

        fun current() = previousInstructions.last()
    }

    data class Instructions(val map: Map<String, Long>) {

        companion object {
            fun of(initialInstruction: String, initialCount: Long = 1L): Instructions {
                val map = mutableMapOf<String, Long>()
                initialInstruction.split("A").dropLast(1).map { "${it}A" }.forEach { map.merge(it, initialCount, Long::plus) }
                return Instructions(map)
            }
        }

        fun groups() = map.map { Pair(it.key, it.value) }

        fun merge(instructions: Instructions): Instructions {
            val localMap = map.toMutableMap()
            instructions.map.forEach { (k, v) -> localMap.merge(k, v, Long::plus) }
            return Instructions(localMap)
        }

        fun length() = map.map { it.value * it.key.length.toLong() }.sum()

    }

    override fun puzzle2(input: Input): Long {
        val codes = input.values

        val numericKeypadMoves = numericKeypadMoves()

        val digitalKeypadMoves = digitalKeypadMoves()

        return codes.sumOf { code ->
            val firstCode = code.split("").filter { it.isNotEmpty() }
            val instructions = listOf("A") + firstCode
            val firstKeypadMoves = getMoves(numericKeypadMoves, instructions.joinToString(""))
            println("code $code: first moves: $firstKeypadMoves")
            val secondMoves = firstKeypadMoves.map { PreviousInstructions(listOf(Instructions.of(it))) }.associateWith { nextInstructionsForNextRobot(it.current()) }
//            secondMoves.forEach { (previousInstruction, nextInstructions) ->
//            println("Instruction $previousInstruction, next instructions $nextInstructions")
//                nextInstructions.groupBy { it.length() }.forEach { group ->
//                    println("Iteration 2 Group size: ${group.key}, count ${group.value.size}")
//                }
//            }
            val flattenSecondMoves = secondMoves.flatMap { (k, v) -> v.map { k.add(it) } }
//            println("Second instructions : ${flattenSecondMoves.map { sm -> sm.previousInstructions[1] }.toSet()}")
            var minLength = flattenSecondMoves.minOf { it.current().length() }
            println("Iteration 2, min length: $minLength")
            val filteredFlattenedSecondMoves = flattenSecondMoves//.filter { it.current().length() == minLength }

            var flattenedNextMoves: List<PreviousInstructions> = flattenSecondMoves
            lateinit var nextMoves: Map<PreviousInstructions, List<Instructions>>

            (3..26).forEach { iteration ->
                nextMoves = flattenedNextMoves.associateWith { nextInstructionsForNextRobot(it.current()) }
                flattenedNextMoves = nextMoves.flatMap { (k, v) -> v.map { k.add(it) } }
                minLength = flattenedNextMoves.minOf { it.current().length() }
                flattenedNextMoves = flattenedNextMoves.filter { it.current().length() == minLength }
                println("Iteration $iteration Min length: ${minLength}")
            }

            minLength = flattenedNextMoves.minOf { it.current().length() }
            code.replace("A", "").toLong() * minLength

            // 77523971440020 too low
            /*

                    val thirdMoves = filteredFlattenedSecondMoves.associateWith { nextInstructionsForNextRobot(it.current()) }
                    thirdMoves.forEach { (previousInstruction, nextInstructions) ->
                        nextInstructions.groupBy { it.length() }.forEach { group ->
                            println("Iteration 3 Group size: ${group.key}, count ${group.value.size}")
                        }
                    }
                    val flattenThirdMoves = thirdMoves.flatMap { (k, v) -> v.map { k.add(it)}}
                    minLength = flattenThirdMoves.minOf { it.current().length() }
                    val filteredFlattenedThirdMoves = flattenThirdMoves.filter { it.current().length() == minLength }
                    println("Third instructions : ${filteredFlattenedThirdMoves.map { sm -> sm.previousInstructions[1] }.toSet()}")
                    println("Iteration 3 Size: ${filteredFlattenedThirdMoves.size}")

                    val fourthMoves = filteredFlattenedThirdMoves.associateWith { nextInstructionsForNextRobot(it.current()) }
                    val flattenFourthMoves = fourthMoves.flatMap { (k, v) -> v.map { k.add(it)}}
                    minLength = flattenFourthMoves.minOf { it.current().length() }
                    val filteredFlattenFourthMoves = flattenFourthMoves.filter { it.current().length() == minLength }
                    println("Fourth instructions : ${filteredFlattenFourthMoves.map { sm -> sm.previousInstructions[1] }.toSet()}")

                    val fifthMoves = filteredFlattenFourthMoves.associateWith { nextInstructionsForNextRobot(it.current()) }
                    val flattenFifthMoves = fifthMoves.flatMap { (k, v) -> v.map { k.add(it)}}
                    minLength = flattenFifthMoves.minOf { it.current().length() }
                    val filteredFlattenFifthMoves = flattenFifthMoves.filter { it.current().length() == minLength }
                    println("Fifth instructions : ${filteredFlattenFifthMoves.map { sm -> sm.previousInstructions[1] }.toSet()}")

                    val sixthMoves = filteredFlattenFifthMoves.associateWith { nextInstructionsForNextRobot(it.current()) }
                    val flattenSixthMoves = sixthMoves.flatMap { (k, v) -> v.map { k.add(it)}}
                    minLength = flattenSixthMoves.minOf { it.current().length() }
                    val filteredFlattenSixthMoves = flattenSixthMoves.filter { it.current().length() == minLength }
                    println("Sixth instructions : ${filteredFlattenSixthMoves.map { sm -> sm.previousInstructions[1] }.toSet()}")

            */

            /*
                    codes.subList(0, 1).forEach { code ->
                        val firstCode = code.split("").filter { it.isNotEmpty() }
                        val instructions = listOf("A") + firstCode
                        val firstKeypadMoves = getMoves(numericKeypadMoves, instructions.joinToString(""))
                        println("code $code: first moves: $firstKeypadMoves")

                        val secondMoves = firstKeypadMoves.associateWith { instructionsForNextRobot(it) }
                        secondMoves.forEach { (previousInstruction, nextInstructions) ->
                            println("Instruction $previousInstruction, next instructions $nextInstructions")
                            nextInstructions.groupBy { it.length }.forEach { group ->
                                println("Group size: ${group.key}, count ${group.value.size}")
                            }
                        }
            *//*

            val minLength = secondMoves.values.minOf { possibleMoves -> possibleMoves.minOf { it.length }}
            val filteredSecondMoves = secondMoves.filter { it.value.minOf { s -> s.length } == minLength }
            filteredSecondMoves.forEach { (previousInstruction, nextInstructions) ->
                println("Filtered instruction $previousInstruction, next instructions $nextInstructions")
                nextInstructions.groupBy { it.length }.forEach { group ->
                    println("Group size: ${group.key}, count ${group.value.size}")
                }
            }
*//*

            val result = (1..24).fold(firstKeypadMoves.last()) { acc, _ -> oneInstructionsForNextRobot(acc)}

            println("Length ${result.length}")
            println(result)

            *//* firstKeypadMoves.subList(2, 3).forEach { firstKeypadMove ->

                 val groups = firstKeypadMove.split("A").dropLast(1).groupingBy { it }.eachCount()
                 val nextGroups = groups.map { (group, count) ->
                     val groupInstructions = "A${group}A"
                     val possibleNextInstructions =
                         groupInstructions.zipWithNext().map { directionalMappings[it]!!.map { s -> "${s}A" } }
                     cartesianProduct(possibleNextInstructions) { it.reduce { acc, s -> acc + s } }
                 }
                 *//**//*var nextGroups2 = nextIteration(mapOf(Pair(firstKeypadMove, 1)))
                (2..3).forEach { i ->
                    nextGroups2 = nextGroups2.flatMap { nextIteration(it) }
                    val minSize = nextGroups2.minOf { m -> m.map { it.value * it.key.length}.sum() }
                    nextGroups2 = nextGroups2.filter { it.size == minSize }
                    println("Iteration $i")
                }*//**//*
               *//**//* var nextInstructions = cartesianProduct(nextGroups) { it.reduce { acc, s -> acc + s } }
                println(nextInstructions)
                (2..5).forEach { iteration ->
                    val nextNextGroups = nextInstructions.first().split("A").dropLast(1).map { group ->
                        val groupInstructions = "A${group}A"
                        val possibleNextInstructions =
                            groupInstructions.zipWithNext().map { directionalMappings[it]!!.map { s -> "${s}A" } }
                        cartesianProduct(possibleNextInstructions) { it.reduce { acc, s -> acc + s } }
                    }
                    nextInstructions = cartesianProduct(nextNextGroups) { it.reduce { acc, s -> acc + s } }
                    println("iteration $iteration")
                }*//**//*
            }*//*
        }
 */
        }
    }

    private fun buildMaps(input: List<List<Pair<String, Int>>>): List<Map<String, Int>> {
        var maps = listOf(mutableMapOf<String, Int>())
        input.forEach { groups ->
            if (groups.size == 1) maps.forEach { it += groups.first() }
            else {
                maps = maps.flatMap { m ->
                    groups.map { g ->
                        val newMap = m.toMutableMap()
                        newMap += g
                        newMap
                    }
                }
            }
        }
        return maps
    }

    private val directionalMappings = mapOf(
        Pair(Pair('<', '<'), setOf("")),
        Pair(Pair('<', 'v'), setOf(">")),
        Pair(Pair('<', '>'), setOf(">>")),
        Pair(Pair('<', '^'), setOf(">^")),
        Pair(Pair('<', 'A'), setOf(/*">^>", */">>^")),
        Pair(Pair('v', 'v'), setOf("")),
        Pair(Pair('v', '<'), setOf("<")),
        Pair(Pair('v', '>'), setOf(">")),
        Pair(Pair('v', '^'), setOf("^")),
        Pair(Pair('v', 'A'), setOf("^>"/*, ">^"*/)),
        Pair(Pair('>', '>'), setOf("")),
        Pair(Pair('>', 'v'), setOf("<")),
        Pair(Pair('>', '<'), setOf("<<")),
        Pair(Pair('>', '^'), setOf("<^"/*, "^<"*/)),
        Pair(Pair('>', 'A'), setOf("^")),
        Pair(Pair('^', '^'), setOf("")),
        Pair(Pair('^', 'v'), setOf("v")),
        Pair(Pair('^', '<'), setOf("v<")),
        Pair(Pair('^', '>'), setOf("v>"/*, ">v"*/)),
        Pair(Pair('^', 'A'), setOf(">")),
        Pair(Pair('A', 'A'), setOf("")),
        Pair(Pair('A', 'v'), setOf("<v", "v<")),
        Pair(Pair('A', '<'), setOf("v<<"/*, "<v<"*/)),
        Pair(Pair('A', '>'), setOf("v")),
        Pair(Pair('A', '^'), setOf("<"))
    )
}