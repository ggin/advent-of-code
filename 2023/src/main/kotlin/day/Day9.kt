package day

import common.DailyChallenge
import common.Input
import kotlin.math.pow
import kotlin.math.sign

class Day9 : DailyChallenge {

    override fun puzzle1(input: Input): Long {
        val positionVisited = mutableSetOf(Pair(0, 0))
        var headPosition = Pair(0, 0);
        var tailPosition = Pair(0, 0);
        val instructions = input.toPairs { it }
        instructions.forEach { (direction, count) ->
            (1..count.toInt()).forEach { _ ->
                headPosition = moveHead(headPosition, direction)
                tailPosition = moveKnot(headPosition, tailPosition)
                positionVisited += tailPosition
            }
        }

        return positionVisited.count().toLong()
    }

    private fun moveHead(headPosition: Pair<Int, Int>, direction: String): Pair<Int, Int> = when (direction) {
        "R" -> Pair(headPosition.first + 1, headPosition.second)
        "U" -> Pair(headPosition.first, headPosition.second + 1)
        "L" -> Pair(headPosition.first - 1, headPosition.second)
        "D" -> Pair(headPosition.first, headPosition.second - 1)
        else -> throw RuntimeException()
    }

    private fun moveKnot(previousKnot: Pair<Int, Int>, knot: Pair<Int, Int>): Pair<Int, Int> {
        val distX = previousKnot.first - knot.first
        val distY = previousKnot.second - knot.second
        val squaredDistance = distX.toDouble().pow(2) + distY.toDouble().pow(2)
        return if (squaredDistance > 2) {
            Pair(knot.first + (1 * distX.sign), knot.second + (1 * distY.sign))
        } else {
            knot
        }
    }


    override fun puzzle2(input: Input) : Long {
        val positionVisited = mutableSetOf(Pair(0, 0))
        var headPosition = Pair(0, 0);
        var knots = (0..8).map { Pair(0, 0)}
        val instructions = input.toPairs { it }
        instructions.forEach { (direction, count) ->
            (1..count.toInt()).forEach { _ ->
                headPosition = moveHead(headPosition, direction)
                knots = knots.runningFold (headPosition) { acc, pair -> moveKnot(acc, pair) }.drop(1)
                positionVisited += knots.last()
            }
        }

        return positionVisited.count().toLong()
    }
}
