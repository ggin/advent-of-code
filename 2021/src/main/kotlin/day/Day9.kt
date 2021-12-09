package day

import common.DailyChallenge

class Day9 : DailyChallenge {

    override fun puzzle1(values: List<String>): Long {
        val map = Map(values)
        return map.getLowPoints().sumOf { it.value + 1L }
    }

    override fun puzzle2(values: List<String>): Long {
        val map = Map(values)
        return map.getLowPoints()
            .map { map.getBasinSizeAt(it, mutableListOf()) }
            .sortedDescending()
            .take(3)
            .reduce { acc, l -> acc * l }
    }

    class Map(values: List<String>) {
        private val coordinates: Array<IntArray> =
            values.map { v ->
                v.split("").filter { it.isNotBlank() }.map { it.toInt() }.toIntArray()
            }.toTypedArray()

        fun getLowPoints() = (coordinates.indices)
            .flatMap { row -> (0 until coordinates[0].size).map { col -> locationAt(row, col)!! } }
            .filter { isLowPoint(it) }

        private fun isLowPoint(l: Location): Boolean {
            return isValueLowerThanPoint(l.value, l.row - 1, l.col)
                    && isValueLowerThanPoint(l.value, l.row + 1, l.col)
                    && isValueLowerThanPoint(l.value, l.row, l.col - 1)
                    && isValueLowerThanPoint(l.value, l.row, l.col + 1)
        }

        private fun isValueLowerThanPoint(value: Int, row: Int, col: Int) =
            coordinates.getOrNull(row)?.getOrNull(col)?.let { value >= it } != true

        private fun locationAt(row: Int, col: Int) =
            if (isOutsideTheMap(row, col)) null else Location(row, col, coordinates[row][col])

        fun getBasinSizeAt(l: Location?, visitedCoordinates: MutableList<Location>): Long {
            if (l == null || visitedCoordinates.any { l.sameCoordinates(it) } || l.value == 9) {
                return 0
            }
            visitedCoordinates.add(l)
            return 1 + getBasinSizeAt(locationAt(l.row - 1, l.col), visitedCoordinates) +
                    getBasinSizeAt(locationAt(l.row + 1, l.col), visitedCoordinates) +
                    getBasinSizeAt(locationAt(l.row, l.col - 1), visitedCoordinates) +
                    getBasinSizeAt(locationAt(l.row, l.col + 1), visitedCoordinates)
        }

        private fun isOutsideTheMap(row: Int, col: Int) =
            row < 0 || row == coordinates.size || col < 0 || col == coordinates[0].size
    }

    class Location(val row: Int, val col: Int, val value: Int) {
        fun sameCoordinates(l: Location) = row == l.row && col == l.col

    }
}
