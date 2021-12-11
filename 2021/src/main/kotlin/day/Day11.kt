package day

import common.DailyChallenge

class Day11 : DailyChallenge {

    companion object {
        const val NB_STEPS = 100
    }

    override fun puzzle1(values: List<String>): Long {
        val g = Grid(values)
        return (1..NB_STEPS).sumOf { _ ->
            g.increaseEnergyLevelForAll()
            g.countFlashesAndCleanup()
        }
    }

    override fun puzzle2(values: List<String>): Long {
        val g = Grid(values)
        return (1..Int.MAX_VALUE).find { _ ->
            g.increaseEnergyLevelForAll()
            g.countFlashesAndCleanup() == g.octopusCount()
        }!!.toLong()
    }

    class Grid(values: List<String>) {
        private val coordinates: Array<IntArray> =
            values.map { v ->
                v.split("").filter { it.isNotBlank() }.map { it.toInt() }.toIntArray()
            }.toTypedArray()

        fun increaseEnergyLevelForAll() =
            coordinates.forEachIndexed { row, _ ->
                coordinates[row].forEachIndexed { col, _ -> increaseEnergyAt(row, col) }
            }

        private fun increaseEnergyAt(row: Int, col: Int) {
            if (!isOutsideTheGrid(row, col)) {
                val newEnergy = coordinates[row][col] + 1
                coordinates[row][col] = newEnergy
                if (newEnergy == 10) {
                    increaseEnergyLevelAround(row, col)
                }
            }
        }

        fun octopusCount() = coordinates.size * coordinates[0].size.toLong()

        private fun increaseEnergyLevelAround(row: Int, col: Int) {
            (-1..1).forEach { x ->
                (-1..1).forEach { y ->
                    increaseEnergyAt(row + x, col + y)
                }
            }
        }

        fun countFlashesAndCleanup(): Long {
            return coordinates.mapIndexed { row, _ ->
                coordinates[row].mapIndexed { col, _ ->
                    if (coordinates[row][col] > 9) {
                        coordinates[row][col] = 0
                        1L
                    } else {
                        0L

                    }
                }
            }.flatten().sum()
        }

        private fun isOutsideTheGrid(row: Int, col: Int) =
            row < 0 || row == coordinates.size || col < 0 || col == coordinates[0].size
    }

}
