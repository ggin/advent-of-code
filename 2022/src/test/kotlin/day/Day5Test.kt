package day

import DailyChallengeTest
import common.DailyChallenge
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day5Test {

    private val challenge = Day5()

    @Test
    fun `puzzle 1`() {
        var result = challenge.puzzle1(challenge.getInput("test-input"))
        assertEquals("CMZ", result)
    }

    @Test
    fun `puzzle 2`() {
        var result = challenge.puzzle2(challenge.getInput("test-input"))
        assertEquals("MCD", result)
    }
}
