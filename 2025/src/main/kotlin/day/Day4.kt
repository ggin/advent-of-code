package day

import common.*

class Day4 : DailyChallenge {
    override fun puzzle1(input: Input): Long {
        val matrix = input.toStringMatrix()
        return matrix.values().count { matrix.isRemovable(it) }.toLong()
    }

    override fun puzzle2(input: Input): Long {
        var matrix = input.toStringMatrix()
        var newMatrix = matrix
        do {
            matrix = newMatrix
            matrix.values().filterIndexed { _, v ->
                v.value == "@" && combineIntoPairs(-1..1).map { Pair(v.x + it.first, v.y + it.second) }.count { v.xy != it && newMatrix.at(it)?.value == "@" } < 4
            }.forEach { newMatrix = newMatrix.set(it.xy, "O") }
        } while (matrix != newMatrix)
        return newMatrix.values().count { it.value == "O" }.toLong()
    }

    fun Matrix<String>.isRemovable(v: Matrix.Value<String>): Boolean {
        return v.value == "@" && combineIntoPairs(-1..1).map { Pair(v.x + it.first, v.y + it.second) }.count { v.xy != it && this.at(it)?.value == "@" } < 4
    }
}