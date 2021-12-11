package day

import DailyChallengeTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day11Test : DailyChallengeTest {

    private val challenge = Day11()

    @Test
    fun `puzzle 1`() {
        val result = puzzle1(challenge)
        assertEquals(1656, result)
    }

    @Test
    fun `puzzle 2`() {
        val result = puzzle2(challenge)
        assertEquals(195, result)
    }
}
