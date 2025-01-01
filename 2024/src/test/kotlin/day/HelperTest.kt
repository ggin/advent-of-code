package day

import common.cartesianProduct
import common.permute
import common.times
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class HelperTest {

    @Test
    fun `test permutations method`() {
        assertEquals(permute(listOf(1, 2, 3)).toSet(), setOf(listOf(1, 2, 3), listOf(1, 3, 2), listOf(2, 1, 3), listOf(2, 3, 1), listOf(3, 1, 2), listOf(3, 2, 1)))
    }

    @Test
    fun `test cartesian product`() {
        val cartesianProduct = listOf(1,2) * listOf("a","b","c")
        assertEquals(listOf(listOf(1,"a"), listOf(1, "b"), listOf(1, "c"), listOf(2, "a"), listOf(2, "b"), listOf(2, "c")), cartesianProduct)
    }

    @Test
    fun `test cartesian product of lists of sets`() {
        val cartesianProduct = cartesianProduct(setOf(setOf("a", "b"), setOf("c"), setOf("d", "e", "f"))) { it.reduce {acc, s -> acc + s} }
        assertEquals(listOf("acd", "ace", "acf", "bcd", "bce", "bcf"), cartesianProduct)
    }

}