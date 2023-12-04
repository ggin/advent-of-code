package day

import common.DailyChallenge
import common.Input

class Day3 : DailyChallenge {

    override fun puzzle1(input: Input): Long {
        var sum = 0L
        val mat = input.values.map { it.toCharArray() }
        mat.forEachIndexed { n, line ->
            var idx = 0
            var number = ""
            var eligible = false
            while (idx < line.size) {
                if (line[idx].isDigit()) {
                    number += line[idx]
                    eligible = eligible || evaluateSymbol(mat, n, idx)
                    if (idx + 1 == line.size || !line[idx + 1].isDigit()) {
                        if (eligible) {
                            sum += number.toLong()
                        }
                        println("Number $number eligible $eligible")
                        number = ""
                        eligible = false
                    }
                }
                idx++
            }
        }

        return sum
    }

    private fun evaluateSymbol(mat: List<CharArray>, x: Int, y: Int) = evaluatePosition(mat, x - 1, y - 1)
            || evaluatePosition(mat, x - 1, y)
            || evaluatePosition(mat, x - 1, y + 1)
            || evaluatePosition(mat, x + 1, y - 1)
            || evaluatePosition(mat, x + 1, y)
            || evaluatePosition(mat, x + 1, y + 1)
            || evaluatePosition(mat, x, y - 1)
            || evaluatePosition(mat, x, y + 1)

    private fun evaluatePosition(mat: List<CharArray>, x: Int, y: Int): Boolean {
        if (x < 0 || y < 0 || x >= mat.size || y >= mat[0].size) return false
        return !mat[x][y].isDigit() && mat[x][y] != '.'
    }

    override fun puzzle2(input: Input): Long {
        val mat = input.values.map { it.toCharArray() }
        val numbers = parseNumbers(mat)
        var ratio = 0L
        mat.forEachIndexed { y, line ->
            line.forEachIndexed { x, c ->
                if (c == '*') {
                    val n = getNeighbours(mat, x, y, numbers)
                    if (n.size == 2) {
                        ratio += n[0].value * n[1].value
                    }
                }
            }
        }
        return ratio
    }

    private fun getNeighbours(mat: List<CharArray>, x: Int, y: Int, l: List<PartNumber>): List<PartNumber> {
        return listOfNotNull(
            findPartNumberAt(x - 1, y - 1, l),
            findPartNumberAt(x - 1, y, l),
            findPartNumberAt(x - 1, y + 1, l),
            findPartNumberAt(x, y - 1, l),
            findPartNumberAt(x, y, l),
            findPartNumberAt(x, y + 1, l),
            findPartNumberAt(x + 1, y - 1, l),
            findPartNumberAt(x + 1, y, l),
            findPartNumberAt(x + 1, y + 1, l)
        ).distinct()
    }

    private fun findPartNumberAt(x: Int, y: Int, l: List<PartNumber>) = l.find { it.y == y && it.xStart <= x && it.xEnd >= x }

    data class PartNumber(val xStart: Int, val xEnd: Int, val y: Int, val value: Long)

    private fun parseNumbers(mat: List<CharArray>): List<PartNumber> {
        val l = mutableListOf<PartNumber>()
        mat.forEachIndexed { y, line ->
            var x = 0
            var number = ""
            while (x < line.size) {
                if (line[x].isDigit()) {
                    number += line[x]
                    if (x + 1 == line.size || !line[x + 1].isDigit()) {
                        l += PartNumber(x - number.length + 1, x, y, number.toLong())
                        number = ""
                    }
                }
                x++
            }
        }
        return l
    }

}
