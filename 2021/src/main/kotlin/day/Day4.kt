package day

import common.DailyChallenge
import java.util.stream.IntStream

class Day4 : DailyChallenge {

    override fun puzzle1(values: List<String>): Long {
        val numbersToDraw = ArrayDeque(values[0].split(",").map { it.trim().toLong() })
        val boards = parseBoards(values)
        var lastNumberDrawn: Long? = null
        while (boards.getWinner() == null) {
            lastNumberDrawn = numbersToDraw.removeFirst()
            boards.onNumberDrawn(lastNumberDrawn)
        }
        val winner = boards.getWinner()!!
        return winner.sumOfUnmatchedNumbers() * lastNumberDrawn!!
    }

    override fun puzzle2(values: List<String>): Long {
        val numbersToDraw = ArrayDeque(values[0].split(",").map { it.trim().toLong() })
        val boards = parseBoards(values)
        var lastNumberDrawn: Long? = null
        while (boards.allWinners().not()) {
            lastNumberDrawn = numbersToDraw.removeFirst()
            boards.onNumberDrawn(lastNumberDrawn)
        }
        val lastWinner = boards.getLoser()
        return lastWinner.sumOfUnmatchedNumbers() * lastNumberDrawn!!
    }

    private fun parseBoards(values: List<String>): Boards {
        val boards = mutableListOf<Board>()
        val parsedRows = mutableListOf<List<String>>()
        values.drop(2).forEach { line ->
            if (line.trim().isEmpty()) {
                boards.add(Board(parsedRows))
                parsedRows.clear()
            } else {
                parsedRows.add(line.split(" ").filter { it.isNotEmpty() })
            }
        }
        boards.add(Board(parsedRows))
        return Boards(ArrayList(boards))
    }

    class Boards(private val boards: List<Board>) {
        private val winners: MutableList<Board> = mutableListOf()

        fun getWinner(): Board? = winners.firstOrNull()
        fun getLoser(): Board = winners.last()
        fun allWinners(): Boolean = boards.all { it.hasWon() }

        fun onNumberDrawn(number: Long) {
            boards.forEach {
                it.onNumberDrawn(number)
                if (it.hasWon() && winners.contains(it).not()) {
                    winners.add(it)
                }
            }
        }
    }

    class Board(rows: List<List<String>>) {
        private val rows: List<Numbers> = rows.map { toNumbers(it) }
        private val columns: List<Numbers> = IntStream.range(0, rows.size).mapToObj { index ->
            toNumbers(rows.map { it[index] })
        }.toList()

        private fun toNumbers(line: List<String>): Numbers = Numbers(line.map { it.toLong() })

        fun onNumberDrawn(number: Long) {
            rows.forEach { it.removeNumber(number) }
            columns.forEach { it.removeNumber(number) }
        }

        fun hasWon(): Boolean = rows.any { it.isEmpty() } || columns.any { it.isEmpty() }

        fun sumOfUnmatchedNumbers(): Long = rows.sumOf { it.sum() }
    }

    class Numbers(values: List<Long>) {
        private val numbers: MutableList<Long> = values.toMutableList()

        fun removeNumber(number: Long) = numbers.remove(number)

        fun sum() = numbers.sum()

        fun isEmpty() = numbers.isEmpty()
    }
}
