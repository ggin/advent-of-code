package day

import common.DailyChallenge
import common.Input
import common.combineIntoPairs

typealias M = List<List<String>>

class Day4 : DailyChallenge {

    private val wordToFind = "XMAS"

    override fun puzzle1(input: Input): Long {
        val content = input.toStringList()
        return content.flatMapIndexed { x, rows ->
            List(rows.size) { y -> combineIntoPairs(x - 1..x + 1, y - 1..y + 1).sumOf { nextPair -> content.wordFound(Pair(x, y), "", nextPair) } }
        }.sum().toLong()
    }


    private tailrec fun M.wordFound(xy: Pair<Int, Int>, wordSoFar: String, xyNext: Pair<Int, Int>): Int {
        val newWord = wordSoFar + at(xy)
        if (newWord == wordToFind) {
            return 1
        }
        if (newWord == wordToFind.substring(0, newWord.length)) {
            val next = Pair(xyNext.first + (xyNext.first - xy.first), xyNext.second + (xyNext.second - xy.second))
            return wordFound(xyNext, newWord, next)
        }
        return 0
    }

    private fun M.at(xy: Pair<Int, Int>): String {
        if (xy.first < 0 || xy.second < 0 || xy.first >= this.size || xy.second >= this[0].size) return "0"
        return this[xy.first][xy.second]
    }

    private fun M.at(x: Int, y: Int) = at(Pair(x, y))

    override fun puzzle2(input: Input): Long {
        val content = input.toStringList()
        return content.flatMapIndexed { x, rows ->
            List(rows.size) { y -> content.matchesPart2(Pair(x, y)) }
        }.sum().toLong()
    }

    private fun M.matchesPart2(xy: Pair<Int, Int>): Int {
        if (at(xy) != "A") return 0
        if (
            ((at(xy.first - 1, xy.second - 1) == "M" && at(xy.first + 1, xy.second + 1) == "S")
                    || (at(xy.first - 1, xy.second - 1) == "S" && at(xy.first + 1, xy.second + 1) == "M"))
            && ((at(xy.first + 1, xy.second - 1) == "M" && at(xy.first - 1, xy.second + 1) == "S")
                    || (at(xy.first + 1, xy.second - 1) == "S" && at(xy.first - 1, xy.second + 1) == "M"))
        ) {
            return 1
        }
        return 0
    }

}