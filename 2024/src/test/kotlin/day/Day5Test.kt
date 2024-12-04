package day

import DailyChallengeTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day5Test : DailyChallengeTest {

    private val challenge = DayTemplate()

    @Test
    fun `puzzle 1`() {
        val result = puzzle1(challenge)
        assertEquals(0L, result)
    }

    @Test
    fun `puzzle 2`() {
        val result = puzzle2(challenge)
        assertEquals(0L, result)
    }
}
