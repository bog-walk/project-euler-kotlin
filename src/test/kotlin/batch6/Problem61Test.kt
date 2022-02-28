package batch6

import kotlin.test.Test
import kotlin.test.assertContentEquals

internal class CyclicalFigurateNumbersTest {
    private val tool = CyclicalFigurateNumbers()

    @Test
    fun `cyclicalFigurates correct for lower constraints`() {
        val polygons = setOf(3, 4, 5)
        val expected = listOf(listOf(2882, 8281, 8128))
        assertContentEquals(expected, tool.cyclicalFigurates(polygons))
    }

    @Test
    fun `cyclicalFigurates correct for mid constraints 1`() {
        val polygons = setOf(3, 4, 5, 6)
        val expected = listOf(
            listOf(1653, 5370, 7021, 2116), listOf(1770, 7021, 2116, 1617)
        )
        assertContentEquals(expected, tool.cyclicalFigurates(polygons))
    }

    @Test
    fun `cyclicalFigurates correct for mid constraints 2`() {
        val polygons = setOf(4, 5, 6, 7)
        val expected = listOf(listOf(1651, 5151, 5192, 9216))
        assertContentEquals(expected, tool.cyclicalFigurates(polygons))
    }

    @Test
    fun `cyclicalFigurates correct for multiple cycles`() {
        val polygons = setOf(3, 4, 6, 7)
        val expected = listOf(
            listOf(2512, 1225, 2556, 5625), listOf(2839, 3916, 1681, 8128)
        )
        assertContentEquals(expected, tool.cyclicalFigurates(polygons))
    }

    @Test
    fun `cyclicalFigurates correct for non-consecutive polygonals`() {
        val polygons = setOf(3, 5, 7)
        val expected = emptyList<List<Int>>()
        assertContentEquals(expected, tool.cyclicalFigurates(polygons))
    }

    @Test
    fun `cyclicalFigurates correct for upper constraints`() {
        val polygons = setOf(3, 4, 5, 6, 7, 8)
        val expected = listOf(listOf(1281, 8128, 2882, 8256, 5625, 2512))
        assertContentEquals(expected, tool.cyclicalFigurates(polygons))
    }
}