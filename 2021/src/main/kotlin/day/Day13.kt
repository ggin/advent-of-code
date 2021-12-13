package day

import common.DailyChallenge
import kotlin.math.abs

class Day13 : DailyChallenge {

    override fun puzzle1(values: List<String>): Long {
        val coordinates = parseCoordinates(values)
        val foldInstructions = values.first { it.startsWith("fold") }
        return fold(coordinates, foldInstructions).count().toLong()
    }

    override fun puzzle2(values: List<String>): Long {
        var coordinates = parseCoordinates(values)
        val foldInstructions = values.filter { it.startsWith("fold") }

        foldInstructions.forEach { instruction ->
            coordinates = fold(coordinates, instruction)
        }
        print(coordinates)
        return coordinates.count().toLong()
    }

    private fun parseCoordinates(values: List<String>) =
        values.filter { it.isNotEmpty() && !it.startsWith("fold") }.map { it.split(",") }
            .map { Pair(it[0].toInt(), it[1].toInt()) }

    private fun fold(
        coordinates: List<Pair<Int, Int>>,
        foldInstructions: String
    ): List<Pair<Int, Int>> {
        val foldHorizontally = foldInstructions.contains("y=")
        val foldValue = foldInstructions.split("=")[1].toInt()
        return fold(
            coordinates,
            if (foldHorizontally) 0 else foldValue,
            if (foldHorizontally) foldValue else 0
        )
    }

    private fun fold(
        coordinates: List<Pair<Int, Int>>,
        foldValueX: Int,
        foldValueY: Int
    ) = coordinates.map { (x, y) ->
        if ((foldValueX == 0 && y < foldValueY) || (foldValueY == 0 && x < foldValueX)) {
            Pair(x, y)
        } else {
            Pair(abs(2 * foldValueX - x), abs(2 * foldValueY - y))
        }
    }.distinct()

    private fun print(coordinates: List<Pair<Int, Int>>) {
        val maxX = coordinates.maxOf { it -> it.second }
        val maxY = coordinates.maxOf { it -> it.first }
        (0..maxX + 1).map { row ->
            val toPrint = coordinates.filter { it.second == row }
            (0..maxY + 1).joinToString("") { c -> if (toPrint.any { it.first == c }) "#" else " " }
        }.forEach { println(it) }
    }
}
