package day

import DailyChallengeTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day2Test : DailyChallengeTest {

    private val challenge = Day2()

    @Test
    fun `puzzle 1`() {
        val result = puzzle1(challenge)
        assertEquals(2L, result)
    }

    @Test
    fun `puzzle 2`() {
        val result = puzzle2(challenge)
        assertEquals(5L, result)
    }
}
