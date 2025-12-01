package day

import DailyChallengeTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import kotlin.math.abs

internal class Day1Test : DailyChallengeTest {

    private val challenge = Day1()

    @Test
    fun `puzzle 1`() {
        val result = puzzle1(challenge)
        assertEquals(3L, result)
    }

    @Test
    fun `puzzle 2`() {
        val result = puzzle2(challenge)
        assertEquals(6L, result)
    }

    @Test
    fun test() {
        assertEquals(Pair(50, 0), logic(50, 0))
        assertEquals(Pair(90, 0), logic(50, 40))
        assertEquals(Pair(0, 1), logic(50, 50))
        assertEquals(Pair(50, 1), logic(50, 100))
        assertEquals(Pair(99, 1), logic(50, 149))
        assertEquals(Pair(0, 2), logic(50, 150))
        assertEquals(Pair(1, 2), logic(50, 151))

        assertEquals(Pair(50, 0), logic(50, -0))
        assertEquals(Pair(10, 0), logic(50, -40))
        assertEquals(Pair(0, 1), logic(50, -50))
        assertEquals(Pair(50, 1), logic(50, -100))
        assertEquals(Pair(1, 1), logic(50, -149))
        assertEquals(Pair(0, 2), logic(50, -150))
        assertEquals(Pair(99, 2), logic(50, -151))

        assertEquals(Pair(0, 1), logic(0, 0))
        assertEquals(Pair(40, 0), logic(0, 40))
        assertEquals(Pair(0, 1), logic(0, 100))
        assertEquals(Pair(50, 1), logic(0, 150))
        assertEquals(Pair(99, 1), logic(0, 199))
        assertEquals(Pair(0, 2), logic(0, 200))
        assertEquals(Pair(1, 2), logic(0, 201))
        assertEquals(Pair(50, 2), logic(0, 250))

        assertEquals(Pair(0, 1), logic(0, -0))
        assertEquals(Pair(60, 0), logic(0, -40))
        assertEquals(Pair(0, 1), logic(0, -100))
        assertEquals(Pair(50, 1), logic(0, -150))
        assertEquals(Pair(1, 1), logic(0, -199))
        assertEquals(Pair(0, 2), logic(0, -200))
        assertEquals(Pair(99, 2), logic(0, -201))
        assertEquals(Pair(50, 2), logic(0, -250))
    }

    fun logic(dial: Int, move: Int): Pair<Int, Int> {
        val newResult = dial + move
        val newResultMod = newResult.mod(100)
        val rForDiv = if (newResult < 0) abs(newResult) + (if (dial == 0) 0 else 100) else newResult
        val countDelta = if (newResultMod == 0 && newResult == 0) 1
        else if (dial == 0 && abs(newResult) < 100) 0
        else if (newResultMod == 0 || newResult != newResultMod) rForDiv.div(100)
        else 0
        return Pair(newResultMod, countDelta)
    }
}
