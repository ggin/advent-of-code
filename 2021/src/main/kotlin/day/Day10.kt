package day

import common.DailyChallenge

typealias Stack = ArrayDeque<Char>

class Day10 : DailyChallenge {

    companion object {
        val SYNTAX_CHECKER_MAPPINGS = mapOf(')' to 3L, ']' to 57L, '}' to 1197L, '>' to 25137L)
        val AUTO_COMPLETE_MAPPINGS = mapOf(')' to 1L, ']' to 2L, '}' to 3L, '>' to 4L)
        val CHAR_MAPPINGS = mapOf('(' to ')', '[' to ']', '{' to '}', '<' to '>')
    }

    override fun puzzle1(values: List<String>) =
        values.mapNotNull { illegalCharacter(it) }
            .sumOf { SYNTAX_CHECKER_MAPPINGS[it]!! }

    override fun puzzle2(values: List<String>) =
        values.filter { illegalCharacter(it) == null }
            .map { missingSequence(it) }
            .map { calculateScore(it) }
            .sorted().let { scores -> scores[scores.size / 2] }

    private fun illegalCharacter(line: String) =
        Stack().let { line.dropWhile { c -> it.updateStack(c) }.firstOrNull() }

    private fun missingSequence(line: String) =
        Stack().apply { line.forEach { updateStack(it) } }

    private fun Char.isOpeningChar() = CHAR_MAPPINGS.containsKey(this)

    private fun Stack.updateStack(c: Char): Boolean =
        if (c.isOpeningChar()) add(CHAR_MAPPINGS[c]!!) else removeLast() == c

    private fun calculateScore(chars: List<Char>) =
        chars.foldRight(0L) { c, score -> score * 5 + AUTO_COMPLETE_MAPPINGS[c]!! }

}
