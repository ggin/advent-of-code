package day

import common.permute
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class HelperTest {

    @Test
    fun `test permutations method`() {
        assertEquals(permute(listOf(1, 2, 3)).toSet(), setOf(listOf(1, 2, 3), listOf(1, 3, 2), listOf(2, 1, 3), listOf(2, 3, 1), listOf(3, 1, 2), listOf(3, 2, 1)))
    }

}