package day

import common.DailyChallenge
import common.Input

typealias TreeMatrix = Array<Array<Int>>
typealias TreeLine = List<Int>

class Day8 : DailyChallenge {

    override fun puzzle1(input: Input) = parse(input)
        .let { matrix ->
            matrix.countIf { x: Int, y: Int ->
                matrix.treeLines(x, y).any { line -> line.all { it < matrix[x][y] } }
            }
        }


    override fun puzzle2(input: Input) = parse(input)
        .let { matrix ->
            matrix.forEachTree { x: Int, y: Int ->
                matrix.treeLines(x, y).fold(1L) { acc, treeLine -> acc * score(treeLine, matrix[x][y]) }
            }.max()
        }
}

private fun score(treeLine: List<Int>, height: Int) =
    treeLine.takeWhile { it < height }.let { it.size + if (it.size < treeLine.size) 1L else 0L }

private fun parse(input: Input): TreeMatrix {
    val charArrays = input.values.map { it.toCharArray() }
    return Array(input.values.size) { x ->
        Array(input.values[x].length) { y -> charArrays[x][y].digitToInt() }
    }
}

private fun TreeMatrix.elementsLeftOf(x: Int, y: Int) = ((x - 1) downTo 0).map { this[it][y] }
private fun TreeMatrix.elementsRightOf(x: Int, y: Int) = ((x + 1) until size).map { this[it][y] }
private fun TreeMatrix.elementsTopOf(x: Int, y: Int) = ((y - 1) downTo 0).map { this[x][it] }
private fun TreeMatrix.elementsBottomOf(x: Int, y: Int) = ((y + 1) until this[x].size).map { this[x][it] }
private fun TreeMatrix.treeLines(x: Int, y: Int): List<TreeLine> =
    listOf(elementsLeftOf(x, y), elementsRightOf(x, y), elementsTopOf(x, y), elementsBottomOf(x, y))

private fun <T> TreeMatrix.forEachTree(apply: (x: Int, y: Int) -> T) = indices.flatMap { x ->
    this[x].indices.map { y -> apply(x, y) }
}

private fun TreeMatrix.countIf(predicate: (x: Int, y: Int) -> Boolean) =
    indices.sumOf { x -> this[x].indices.count { y -> predicate(x, y) } }.toLong()
