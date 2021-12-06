package day

import DailyChallengeTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day6Test : DailyChallengeTest {

    private val challenge = Day6()

    @Test
    fun `puzzle 1`() {
        val result = puzzle1(challenge)
        assertEquals(5934, result)
    }

    @Test
    fun `puzzle 2`() {
        val result = puzzle2(challenge)
        assertEquals(26984457539, result)
    }
}
