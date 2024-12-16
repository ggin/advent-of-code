package day

import common.DailyChallenge
import common.Input
import common.Matrix
import common.toStringMatrix

private const val ANSWER = 73404L

class Day16 : DailyChallenge {
    override fun puzzle1(input: Input): Long {
        val matrix = input.toStringMatrix()
        val reindeer = matrix.findFirst("S")!!
        val arrival = matrix.findFirst("E")!!
        visited[Triple(reindeer.x, reindeer.y, Direction.RIGHT)] = 0L
        return solve(matrix, reindeer.xy, arrival.xy, 0L, Direction.RIGHT)!!
    }

    enum class Direction(val move: (Pair<Int, Int>) -> Pair<Int, Int>) {
        UP({ p -> Pair(p.first - 1, p.second) }),
        LEFT({ p -> Pair(p.first, p.second - 1) }),
        DOWN({ p -> Pair(p.first + 1, p.second) }),
        RIGHT({ p -> Pair(p.first, p.second + 1) })
    }

    private var visited: MutableMap<Triple<Int, Int, Direction>, Long> = mutableMapOf()

    private fun solve(
        matrix: Matrix<String>,
        reindeer: Pair<Int, Int>,
        arrival: Pair<Int, Int>,
        score: Long,
        direction: Direction
    ): Long? {
        val newMove = direction.move(reindeer)
        val scorePlusOne = score + 1
        var scoreMoving: Long? = null
        val cacheKey = Triple(newMove.first, newMove.second, direction)
        val cacheValue = visited[cacheKey]
        if (cacheValue != null) {
            if (scorePlusOne < cacheValue) {
                visited[cacheKey] = scorePlusOne
                scoreMoving = if (newMove == arrival) scorePlusOne else solve(matrix, newMove, arrival, scorePlusOne, direction)
            }
        } else if (newMove == arrival) {
            visited[cacheKey] = scorePlusOne
            scoreMoving = scorePlusOne
        } else if (matrix.at(newMove)?.value != "#") {
            visited[cacheKey] = scorePlusOne
            scoreMoving = solve(matrix, newMove, arrival, scorePlusOne, direction)
        }

        val scoresTurning = (1..3).mapNotNull { i ->
            val d = Direction.entries[(direction.ordinal + i).mod(4)]
            val newMoveAfterTurn = d.move(reindeer)
            val newScoreAfterTurnAndMove = score + 1 + (if (i == 2) 2000 else 1000)

            val cacheKey = Triple(newMoveAfterTurn.first, newMoveAfterTurn.second, d)
            val cacheValue = visited[cacheKey]
            if (cacheValue != null) {
                if (newScoreAfterTurnAndMove < cacheValue) {
                    visited[cacheKey] = newScoreAfterTurnAndMove
                    if (newMoveAfterTurn == arrival) newScoreAfterTurnAndMove else solve(matrix, newMoveAfterTurn, arrival, newScoreAfterTurnAndMove, d)
                } else {
                    null
                }
            } else if (newMoveAfterTurn == arrival) {
                visited[cacheKey] = newScoreAfterTurnAndMove
                newScoreAfterTurnAndMove
            } else if (matrix.at(newMoveAfterTurn)?.value == "#") {
                null
            } else {
                visited[cacheKey] = newScoreAfterTurnAndMove
                solve(matrix, newMoveAfterTurn, arrival, newScoreAfterTurnAndMove, d)
            }
        }.minOrNull()

        return listOfNotNull(scoreMoving, scoresTurning).minOrNull()
    }

    private val solutions : MutableList<List<Direction>> = mutableListOf()

    private fun solvePart2(
        matrix: Matrix<String>,
        reindeer: Pair<Int, Int>,
        arrival: Pair<Int, Int>,
        score: Long,
        directions: List<Direction>
    ): Long? {
        if (score > ANSWER) return null
        val direction  = directions.last()
        val newMove = direction.move(reindeer)
        val scorePlusOne = score + 1
        var scoreMoving: Long? = null
        val cacheKey = Triple(newMove.first, newMove.second, direction)
        val cacheValue = visited[cacheKey]
        if (cacheValue != null) {
            if (scorePlusOne <= cacheValue) {
                visited[cacheKey] = scorePlusOne
                scoreMoving = if (newMove == arrival) {
                    if (scorePlusOne == ANSWER) {
                        println("Breakpoint 1")
                        solutions += directions + direction
                    }
                    scorePlusOne
                } else solvePart2(matrix, newMove, arrival, scorePlusOne, directions + direction)
            }
        } else if (newMove == arrival) {
            visited[cacheKey] = scorePlusOne
            if (scorePlusOne == ANSWER) {
                println("Breakpoint 2")
                solutions += directions + direction
            }
            scoreMoving = scorePlusOne
        } else if (matrix.at(newMove)?.value != "#") {
            visited[cacheKey] = scorePlusOne
            scoreMoving = solvePart2(matrix, newMove, arrival, scorePlusOne, directions + direction)
        }

        val scoresTurning = (1..3).mapNotNull { i ->
            val d = Direction.entries[(direction.ordinal + i).mod(4)]
            val newMoveAfterTurn = d.move(reindeer)
            val newScoreAfterTurnAndMove = score + 1 + (if (i == 2) 2000 else 1000)

            val cacheKey = Triple(newMoveAfterTurn.first, newMoveAfterTurn.second, d)
            val cacheValue = visited[cacheKey]
            if (cacheValue != null) {
                if (newScoreAfterTurnAndMove <= cacheValue) {
                    visited[cacheKey] = newScoreAfterTurnAndMove
                    if (newMoveAfterTurn == arrival) {
                        if (newScoreAfterTurnAndMove == ANSWER) {
                            println("Breakpoint 3")
                            solutions += directions + d
                        }
                        newScoreAfterTurnAndMove
                    } else solvePart2(matrix, newMoveAfterTurn, arrival, newScoreAfterTurnAndMove, directions + d)
                } else {
                    null
                }
            } else if (newMoveAfterTurn == arrival) {
                visited[cacheKey] = newScoreAfterTurnAndMove
                if (newScoreAfterTurnAndMove == ANSWER) {
                    println("Breakpoint 4")
                    solutions += directions + d
                }
                newScoreAfterTurnAndMove
            } else if (matrix.at(newMoveAfterTurn)?.value == "#") {
                null
            } else {
                visited[cacheKey] = newScoreAfterTurnAndMove
                solvePart2(matrix, newMoveAfterTurn, arrival, newScoreAfterTurnAndMove, directions + d)
            }
        }.minOrNull()

        return listOfNotNull(scoreMoving, scoresTurning).minOrNull()
    }

    override fun puzzle2(input: Input): Long {
        val matrix = input.toStringMatrix()
        val reindeer = matrix.findFirst("S")!!
        val arrival = matrix.findFirst("E")!!
        visited[Triple(reindeer.x, reindeer.y, Direction.RIGHT)] = 0L
        solvePart2(matrix, reindeer.xy, arrival.xy, 0L, listOf(Direction.RIGHT))!!

        val positionSet : MutableSet<Pair<Int, Int>> = mutableSetOf()
        solutions.forEach { l ->
            l.foldRight(reindeer.xy) { d, acc ->
                val newPos = d.move(acc)
                positionSet += newPos
                newPos
            }
        }
        return positionSet.count().toLong()
    }





}