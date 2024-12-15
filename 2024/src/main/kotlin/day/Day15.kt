package day

import common.DailyChallenge
import common.Input
import common.Matrix
import common.Matrix.*
import java.util.*

class Day15 : DailyChallenge {
    override fun puzzle1(input: Input): Long {
        val map: MutableList<String> = mutableListOf()
        var i = ""
        input.values.forEach { line ->
            if (line.startsWith("#")) map += line
            else if (i != System.lineSeparator()) i += line
        }
        var matrix = Matrix(map.map { it.split("").filter { s -> s.isNotBlank() } }.toList())
        val instructions = LinkedList(i.split("").filter { s -> s.isNotBlank() }.toList())
        while (instructions.isNotEmpty()) {
            val instruction = instructions.pop()
            val robot = matrix.findFirst("@")!!
            when (instruction) {
                "^" -> {
                    if (matrix.at(robot.x - 1, robot.y)?.value == ".") {
                        matrix = matrix.set(robot.x, robot.y, ".").set(robot.x - 1, robot.y, "@")
                    } else if (matrix.at(robot.x - 1, robot.y)?.value == "O") {
                        (robot.x - 2 downTo 0).takeWhile { setOf(".", "O").contains(matrix.at(it, robot.y)?.value) }.firstOrNull { matrix.at(it, robot.y)?.value == "." }?.apply {
                            matrix = matrix.set(robot.x, robot.y, ".").set(robot.x - 1, robot.y, "@").set(this, robot.y, "O")
                        }
                    }
                }

                "v" -> {
                    if (matrix.at(robot.x + 1, robot.y)?.value == ".") {
                        matrix = matrix.set(robot.x, robot.y, ".").set(robot.x + 1, robot.y, "@")
                    } else if (matrix.at(robot.x + 1, robot.y)?.value == "O") {
                        (robot.x + 2..matrix.xCount).takeWhile { setOf(".", "O").contains(matrix.at(it, robot.y)?.value) }.firstOrNull { matrix.at(it, robot.y)?.value == "." }?.apply {
                            matrix = matrix.set(robot.x, robot.y, ".").set(robot.x + 1, robot.y, "@").set(this, robot.y, "O")
                        }
                    }
                }

                ">" -> {
                    if (matrix.at(robot.x, robot.y + 1)?.value == ".") {
                        matrix = matrix.set(robot.x, robot.y, ".").set(robot.x, robot.y + 1, "@")
                    } else if (matrix.at(robot.x, robot.y + 1)?.value == "O") {
                        (robot.y + 2..matrix.yCount).takeWhile { setOf(".", "O").contains(matrix.at(robot.x, it)?.value) }.firstOrNull { matrix.at(robot.x, it)?.value == "." }?.apply {
                            matrix = matrix.set(robot.x, robot.y, ".").set(robot.x, robot.y + 1, "@").set(robot.x, this, "O")
                        }
                    }
                }

                "<" -> {
                    if (matrix.at(robot.x, robot.y - 1)?.value == ".") {
                        matrix = matrix.set(robot.x, robot.y, ".").set(robot.x, robot.y - 1, "@")
                    } else if (matrix.at(robot.x, robot.y - 1)?.value == "O") {
                        (robot.y - 2 downTo 0).takeWhile { setOf(".", "O").contains(matrix.at(robot.x, it)?.value) }.firstOrNull { matrix.at(robot.x, it)?.value == "." }?.apply {
                            matrix = matrix.set(robot.x, robot.y, ".").set(robot.x, robot.y - 1, "@").set(robot.x, this, "O")
                        }
                    }
                }
            }
        }
        matrix.print()
        return matrix.findAll("O").sumOf {
            it.x * 100L + it.y
        }
    }

    override fun puzzle2(input: Input): Long {
        val map: MutableList<String> = mutableListOf()
        var i = ""
        input.values.forEach { line ->
            if (line.startsWith("#")) map += line.replace("#", "##").replace("O", "[]").replace(".", "..").replace("@", "@.")
            else if (i != System.lineSeparator()) i += line
        }
        var matrix = Matrix(map.map { it.split("").filter { s -> s.isNotBlank() } }.toList())
        val instructions = LinkedList(i.split("").filter { s -> s.isNotBlank() }.toList())
        while (instructions.isNotEmpty()) {
            val instruction = instructions.pop()
            val robot = matrix.findFirst("@")!!
            when (instruction) {
                "^" -> matrix = moveRobot(matrix, robot.xy) {p: Pair<Int, Int> -> Pair(p.first - 1, p.second) }
                "v" -> matrix = moveRobot(matrix, robot.xy) {p: Pair<Int, Int> -> Pair(p.first + 1, p.second) }
                ">" -> matrix = moveRobot(matrix, robot.xy) {p: Pair<Int, Int> -> Pair(p.first, p.second + 1) }
                "<" -> matrix = moveRobot(matrix, robot.xy) {p: Pair<Int, Int> -> Pair(p.first, p.second - 1) }
            }
        }
        matrix.print()
        return matrix.findAll("[").sumOf {
            it.x * 100L + it.y
        }
    }

    private fun moveUp(matrix: Matrix<String>, robot: Value<String>): Matrix<String> {
        if (matrix.at(robot.x - 1, robot.y)?.value == ".") return matrix.set(robot.x, robot.y, ".").set(robot.x - 1, robot.y, "@")
        return matrix
    }

    fun moveRobot(matrix: Matrix<String>, robot: Pair<Int, Int>, move: (Pair<Int, Int>) -> Pair<Int, Int>) : Matrix<String> {
        val newRobot = move(robot)
        if (matrix.at(newRobot)?.value == ".") return matrix.set(robot, ".").set(newRobot, "@")
        if (matrix.at(newRobot)?.value == "#") return matrix
        if (matrix.at(newRobot)?.value == "[") {
            val newMatrix = matrix.set(robot, ".").set(newRobot, "@").set(newRobot.first, newRobot.second + 1, ".")
            return moveBoxes(newRobot, Pair(newRobot.first, newRobot.second + 1), move, newMatrix) ?: matrix
        }
        if (matrix.at(newRobot)?.value == "]") {
            val newMatrix = matrix.set(robot, ".").set(newRobot, "@").set(newRobot.first, newRobot.second - 1, ".")
            return moveBoxes(Pair(newRobot.first, newRobot.second - 1), newRobot, move, newMatrix) ?: matrix
        }
        return matrix
    }

    private fun moveBoxes(leftBox: Pair<Int, Int>, rightBox: Pair<Int, Int>, move: (Pair<Int, Int>) -> Pair<Int, Int>, matrix: Matrix<String>) : Matrix<String>? {
        val newLeftBox = move(leftBox)
        val newRightBox = move(rightBox)
        if (matrix.at(newLeftBox)?.value == "." && matrix.at(newRightBox)?.value == ".") return matrix.set(newLeftBox, "[").set(newRightBox, "]")
        if (matrix.at(newLeftBox)?.value == "#" || matrix.at(newRightBox)?.value == "#") return null

        if (matrix.at(newLeftBox)?.value == "[" && matrix.at(newRightBox)?.value == "]") {
            return moveBoxes(newLeftBox, newRightBox, move, matrix)
        }
        var newMatrix: Matrix<String>? = matrix
        if (newMatrix?.at(newLeftBox)?.value == "]") {
            newMatrix = newMatrix.set(newLeftBox.first, newLeftBox.second - 1, ".")
            newMatrix = moveBoxes(Pair(newLeftBox.first, newLeftBox.second - 1), newLeftBox, move, newMatrix)
        }
        if (newMatrix?.at(newRightBox)?.value == "[") {
            newMatrix = newMatrix.set(newRightBox.first, newRightBox.second + 1, ".")
            newMatrix = moveBoxes(newRightBox, Pair(newRightBox.first, newRightBox.second + 1), move, newMatrix)
        }
        return newMatrix?.set(newLeftBox, "[")?.set(newRightBox, "]")
/*
        var newMatrix = matrix
        if (matrix.at(newLeftBox)?.value == "]") {
            newMatrix
        }

        if (matrix.at(newLeftBox)?.value == "]" && matrix.at(newRightBox)?.value == "[") {
            val newMatrix = matrix.set(leftBox, ".").set(rightBox, ".").set(newLeftBox, "[").set(newRightBox, "]")
            moveBoxes()
        }

        if (newBoxes.map { matrix.at(it)?.value}.toList() == listOf("[", "]")) return canBoxMove(newBoxes, move, matrix)
        return newBoxes.filter { setOf("[", "]").contains(matrix.at(it)?.value) }
            .all( newBox -> canBoxMove(newBoxes, move, matrix))

        return boxes.first.x > 0 && boxes.second.x > 0 && matrix.at(boxes.first.x - 1, boxes.first.y)?.value == "." && matrix.at(boxes.second.x - 1, boxes.second.y)?.value == "."
*/
    }
}