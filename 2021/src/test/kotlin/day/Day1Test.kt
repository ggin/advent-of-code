package day

import DailyChallengeTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day1Test : DailyChallengeTest {

    private val challenge = Day1()

    @Test
    fun `puzzle 1`() {
        val result = puzzle1(challenge)
        assertEquals(514579, result)
    }

    @Test
    fun `puzzle 2`() {
        val result = puzzle2(challenge)
        assertEquals(241861950, result)
    }
}
