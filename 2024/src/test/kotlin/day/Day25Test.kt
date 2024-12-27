package day

import DailyChallengeTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day25Test : DailyChallengeTest {

    private val challenge = Day25()

    @Test
    fun `puzzle 1`() {
        val result = puzzle1(challenge)
        assertEquals(3L, result)
    }

    @Test
    fun `puzzle 2`() {
        val result = puzzle2(challenge)
        assertEquals(0L, result)
    }
}
