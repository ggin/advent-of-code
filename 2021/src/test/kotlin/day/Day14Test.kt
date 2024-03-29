package day

import DailyChallengeTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day14Test : DailyChallengeTest {

    private val challenge = Day14()

    @Test
    fun `puzzle 1`() {
        val result = puzzle1(challenge)
        assertEquals(1588, result)
    }

    @Test
    fun `puzzle 2`() {
        val result = puzzle2(challenge)
        assertEquals(2188189693529, result)
    }
}
