package day

import DailyChallengeTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day15Test : DailyChallengeTest {

    private val challenge = Day15()

    @Test
    fun `puzzle 1`() {
        val result = puzzle1(challenge)
        assertEquals(10092L, result)
    }

    @Test
    fun `puzzle 2`() {
        val result = puzzle2(challenge)
        assertEquals(9021L, result)
    }

}
