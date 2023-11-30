package day

import DailyChallengeTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day9Test : DailyChallengeTest {

    private val challenge = Day9()

    @Test
    fun `puzzle 1`() {
        val result = puzzle1(challenge)
        assertEquals(13L, result)
    }

    @Test
    fun `puzzle 2`() {
        val result = challenge.puzzle2(challenge.getInput("test-input-part2"))
        assertEquals(36L, result)
    }
}
